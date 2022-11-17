package ind.liuer.rabbitmq.basic.hello$world;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import ind.liuer.rabbitmq.utils.RabbitMqUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuJie
 * @date 2022-11-07
 * @since 1.0
 */
@Slf4j
public class Consumer {

    @SuppressWarnings("all")
    public void receive() throws IOException {
        // 获取RabbitMQ通道
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机, 省略(使用默认交换机: 名称为空串, 类型为direct)

        // 声明队列
        // queue(队列名称)、durable(是否持久化)、exclusive(是否独占)、autoDelete(是否自动删除)、args(其他参数)
        boolean durable = false;
        boolean exclusive = false;
        boolean autoDelete = false;
        Map<String, Object> args = new HashMap<>();
        channel.queueDeclare(Constant.QUEUE_NAME, durable, exclusive, autoDelete, args);

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
        channel.basicConsume(Constant.QUEUE_NAME, autoAck, deliverCallback, cancelCallback);

        log.info("按下回车键结束程序......");
        System.in.read();

        // 回收资源
        RabbitMqUtils.closeAll(channel);
    }

    public static void main(String[] args) throws IOException {
        new Consumer().receive();
    }
}
