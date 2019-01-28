package org.windwant.bus.mq;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.bus.util.RabbitMqUtils;
import org.windwant.util.ConfigUtil;

import java.io.IOException;

/**
 * 消息处理
 * Created by Administrator on 18-4-11.
 */
public class RabbitMqListener implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);

    public void run(){
        Channel channel = null;
        try {
            channel = RabbitMqUtils.getConnection().createChannel();
            channel.queueDeclare(ConfigUtil.get("bus.websocket.msg_queue"), true, true, false, null);
            channel.exchangeDeclare(ConfigUtil.get("rabbitmq.exchange.faout"), BuiltinExchangeType.FANOUT);
            channel.queueBind(ConfigUtil.get("bus.websocket.msg_queue"),
                    ConfigUtil.get("rabbitmq.exchange.faout"),
                    ConfigUtil.get("rabbitmq.bus.route_key"));
            logger.info("rabbitmq message queue listener start...");
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    MqMsgHandler.handleMsg(new String(body));
                }
            };
            channel.basicConsume(ConfigUtil.get("bus.websocket.msg_queue"), consumer);
        }catch (Exception e){
            logger.error("rabbitmq message queue listener start failed: {}", e.getMessage());
        }
    }
}
