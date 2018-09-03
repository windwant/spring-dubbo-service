package org.windwant.bus.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.windwant.util.ConfigUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by windwant on 2016/8/15.
 */
public class RabbitMqUtils {
    private static ConnectionFactory connectionFactory;

    public static ConnectionFactory getConnectionFactory() throws ConfigurationException {
        if(connectionFactory == null){
            connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(ConfigUtil.get("rabbitmq.host"));
            connectionFactory.setPort(ConfigUtil.getInteger("rabbitmq.port"));
            connectionFactory.setUsername(ConfigUtil.get("rabbitmq.user"));
            connectionFactory.setPassword(ConfigUtil.get("rabbitmq.passwd"));
        }
        return connectionFactory;
    }

    public static Connection getConnection() throws ConfigurationException, IOException, TimeoutException {
        return getConnectionFactory().newConnection();
    }
}
