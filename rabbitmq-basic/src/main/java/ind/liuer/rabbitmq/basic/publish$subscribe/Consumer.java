package ind.liuer.rabbitmq.basic.publish$subscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import ind.liuer.rabbitmq.utils.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author LiuJie
 * @date 2022-11-13
 * @since 1.0
 */
@Slf4j
public class Consumer {

    @SuppressWarnings("all")
    public void receive() throws IOException {
        // 获取RabbitMQ通道
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        // exchange(交换机名称), type(交换机类型)
        channel.exchangeDeclare(Constant.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        // 声明队列(随机队列名称)
        String queueName = channel.queueDeclare().getQueue();

        // 绑定队列到交换机
        // queue(队列名称), exchange(交换机名称), routingKey(路由key)
        String routingKey = "";
        channel.queueBind(queueName, Constant.EXCHANGE_NAME, routingKey);

        log.info("等待消息中......");

        // 接收消息
        // queue(队列名称), autoAck(自动签收), deliverCallback(接收回调), cancelCallback(取消回调)
        boolean autoAck = true;
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("接收消息[{}]成功", message);
        };
        CancelCallback cancelCallback = consumerTag -> {
        };
        channel.basicConsume(queueName, autoAck, deliverCallback, cancelCallback);

        log.info("按下回车键结束程序......");
        System.in.read();

        // 回收资源
        RabbitMqUtils.closeAll(channel);
    }

    public static void main(String[] args) throws IOException {
        new Consumer().receive();
    }
}
