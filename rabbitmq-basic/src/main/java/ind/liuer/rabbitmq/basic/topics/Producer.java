package ind.liuer.rabbitmq.basic.topics;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import ind.liuer.rabbitmq.basic.domain.RoutingMessage;
import ind.liuer.rabbitmq.utils.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author LiuJie
 * @date 2022-11-16
 * @since 1.0
 */
@Slf4j
public class Producer {

    @SuppressWarnings("all")
    public void send() throws IOException {
        // 获取RabbitMQ通道
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        // exchange(交换机名称), type(交换机类型)
        channel.exchangeDeclare(Constant.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 声明队列, 省略(消费者处声明)

        // 发送消息
        // exchange(交换机名称)、routingKey(路由key)、props(其他参数), body(消息内容)
        for (RoutingMessage routingMessage : Constant.MESSAGES) {
            String routingKey = routingMessage.getRoutingKey();
            AMQP.BasicProperties props = new AMQP.BasicProperties();
            String message = routingMessage.getMessage();
            byte[] body = message.getBytes(StandardCharsets.UTF_8);
            channel.basicPublish(Constant.EXCHANGE_NAME, routingKey, props, body);
            log.info("发送消息[{}]成功", message);
        }

        log.info("发布消息完成");

        // 回收资源
        RabbitMqUtils.closeAll(channel);
    }

    public static void main(String[] args) throws IOException {
        new Producer().send();
    }
}
