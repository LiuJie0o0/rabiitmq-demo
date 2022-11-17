package ind.liuer.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * @author LiuJie
 * @date 2022-11-07
 * @since 1.0
 */
@Slf4j
public class RabbitMqUtils {

    public static final String propsFilename = "rabbitmq.properties";

    public static final Properties props = new Properties();

    public static final ConnectionFactory connFactory = new ConnectionFactory();

    private static Connection connection;

    static {
        refreshProperties();
    }

    /**
     * 获取RabbitMQ通道
     */
    public static Channel getChannel() {
        connFactory.setHost(props.getProperty("rabbitmq.host"));
        connFactory.setVirtualHost(props.getProperty("rabbitmq.virtual-host"));
        connFactory.setUsername(props.getProperty("rabbitmq.username"));
        connFactory.setPassword(props.getProperty("rabbitmq.password"));
        try {
            connection = connFactory.newConnection();
            return connection.createChannel();
        } catch (Exception e) {
            log.error("RabbitMQ连接获取失败: [{}]", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭当前RabbitMQ通道
     */
    public static void closeChannel(Channel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 关闭RabbitMQ连接
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 关闭当前RabbitMQ通道以及RabbitMQ连接
     */
    public static void closeAll(Channel channel) {
        closeChannel(channel);
        closeConnection();
    }

    /**
     * 加载RabbitMQ配置
     */
    public static void refreshProperties() {
        InputStream inputStream = FileUtils.getClasspathFileStream(propsFilename);
        try {
            props.load(inputStream);
        } catch (IOException e) {
            log.error("RabbitMQ配置初始化失败: [{}]", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
