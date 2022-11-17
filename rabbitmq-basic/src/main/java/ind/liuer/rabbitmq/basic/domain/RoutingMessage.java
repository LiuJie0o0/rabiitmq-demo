package ind.liuer.rabbitmq.basic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author LiuJie
 * @date 2022-11-15
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class RoutingMessage {

    /**
     * 路由key
     */
    private String routingKey;

    /**
     * 消息内容
     */
    private String message;
}
