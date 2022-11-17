package ind.liuer.rabbitmq.basic.publisher$confirms;

/**
 * @author LiuJie
 * @date 2022-11-17
 * @since 1.0
 */
public class Constant {

    /**
     * 发送消息次数
     */
    public static final int TIMES = 1000;

    /**
     * 批量确认消息条数
     */
    public static final int BATCH_SIZE = 100;

    /**
     * 超时毫秒数
     */
    public static final int TIMEOUT = 5000;

    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "exc.confirm";

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "queue.confirm";

    public static String num2String(int num) {
        if (num < 0 || num >= Constant.TIMES) {
            throw new RuntimeException("不合法编号");
        }
        if (num <= 9) {
            return "00" + (num + 1);
        } else if (num <= 99) {
            return "0" + (num + 1);
        } else {
            return "" + (num + 1);
        }
    }
}
