package ind.liuer.rabbitmq.basic.publisher$confirms;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import ind.liuer.rabbitmq.utils.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author LiuJie
 * @date 2022-11-17
 * @since 1.0
 */
@Slf4j
public class ProducerAsynchronously {

    @SuppressWarnings("all")
    public void send() throws IOException, InterruptedException, TimeoutException {
        // 获取RabbitMQ通道
        Channel channel = RabbitMqUtils.getChannel();

        // 开启发布确认策略
        channel.confirmSelect();

        // 声明交换机
        // exchange(交换机名称), type(交换机类型)
        channel.exchangeDeclare(Constant.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 声明队列, 省略(消费者处声明)

        // 绑定队列到交换机
        // queue(队列名称), exchange(交换机名称), routingKey(路由key)
        channel.queueBind(Constant.QUEUE_NAME, Constant.EXCHANGE_NAME, Constant.QUEUE_NAME);

        // 添加消息异步确认监听器
        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        ConfirmCallback clearOutstandingConfirms = (deliveryTag, multiple) -> {
            boolean inclusive = true;
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag, inclusive);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
        };
        channel.addConfirmListener(clearOutstandingConfirms, (deliveryTag, multiple) -> {
                String body = outstandingConfirms.get(deliveryTag);
                log.error("消息[{}]未被确认 => {DeliveryTag: [{}], Multiple: [{}]}", body, deliveryTag, multiple);
                clearOutstandingConfirms.handle(deliveryTag, multiple);
            }
        );

        // 发送消息
        // exchange(交换机名称)、routingKey(路由key)、props(其他参数), body(消息内容)
        AMQP.BasicProperties props = new AMQP.BasicProperties();
        String message = "Hello, Confirm Message";
        long start = System.currentTimeMillis();
        for (int i = 0; i < Constant.TIMES; i++) {
            String finalMessage = Constant.num2String(i) + " => " + message;
            byte[] body = finalMessage.getBytes(StandardCharsets.UTF_8);
            long deliveryTag = channel.getNextPublishSeqNo() - 1L;
            outstandingConfirms.put(deliveryTag, finalMessage);
            channel.basicPublish(Constant.EXCHANGE_NAME, Constant.QUEUE_NAME, props, body);
            log.info("发送异步确认消息[{}]成功", finalMessage);
        }
        long over = System.currentTimeMillis();
        log.info("发送异步确认消息共耗时[{}]毫秒", (over - start));

        log.info("发布消息完成");

        // 回收资源
        RabbitMqUtils.closeAll(channel);
    }

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        new ProducerAsynchronously().send();
    }
}
