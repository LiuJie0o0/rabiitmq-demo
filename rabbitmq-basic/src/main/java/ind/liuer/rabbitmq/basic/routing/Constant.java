package ind.liuer.rabbitmq.basic.routing;

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
    public static final String EXCHANGE_NAME = "exc.routing";

    /**
     * 文件路由keys
     */
    public static final String[] FILE_KEYS = new String[]{"info"};

    /**
     * 控制台路由keys
     */
    public static final String[] CONSOLE_KEYS = new String[]{"debug", "info"};

    /**
     * 发送消息的路由keys
     */
    public static final String[] ROUTING_KEYS = new String[]{"debug", "info"};

    /**
     * 发送消息列表
     */
    public static final List<RoutingMessage> MESSAGES = new ArrayList<>();

    static {
        // 添加消息
        String message = "Hello, Routing Message";
        for (int i = 0; i < 10; i++) {
            int num = (int) Math.round(Math.random() * (ROUTING_KEYS.length - 1));
            String routingKey = ROUTING_KEYS[num];
            String finalMessage = "0" + i + " => " + message + " (" + routingKey + ")";
            RoutingMessage routingMessage = new RoutingMessage(routingKey, finalMessage);
            MESSAGES.add(routingMessage);
        }
    }
}
