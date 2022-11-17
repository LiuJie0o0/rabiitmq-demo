package ind.liuer.rabbitmq.basic.topics;

import ind.liuer.rabbitmq.basic.domain.RoutingMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuJie
 * @date 2022-11-16
 * @since 1.0
 */
public class Constant {

    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "exc.topic";

    /**
     * 猫猫路由keys
     */
    public static final String[] CAT_KEYS = new String[]{"*.orange.*"};

    /**
     * 狗狗路由keys
     */
    public static final String[] DOG_KEYS = new String[]{"*.*.rabbit", "lazy.#"};

    /**
     * 发送消息的路由keys
     */
    public static final String[] ROUTING_KEYS = new String[]{"lazy.orange.rabbit", "lazy.white.cat", "clever.pink.rabbit", "laborious.brown.mouse", "boring.orange.mouse"};

    /**
     * 发送消息列表
     */
    public static final List<RoutingMessage> MESSAGES = new ArrayList<>();

    static {
        // 添加消息
        String message = "Hello, Topics Message";
        for (int i = 0; i < 10; i++) {
            int num = (int) Math.round(Math.random() * (ROUTING_KEYS.length - 1));
            String routingKey = ROUTING_KEYS[num];
            String finalMessage = "0" + i + " => " + message + " (" + routingKey + ")";
            RoutingMessage routingMessage = new RoutingMessage(routingKey, finalMessage);
            MESSAGES.add(routingMessage);
        }
    }
}
