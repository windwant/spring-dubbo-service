package org.windwant.test;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.ConfigUtil;
import org.windwant.test.util.ConsulUtil;
import org.windwant.test.util.JedisUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 获取consul 当前连接通道信息，
 * 向redis 推送队列添加每一个通道需要推送的json消息 测试长连接消息推送
 * Created by Administrator on 18-4-11.
 */
public class MsgQueueMsgTest {
    private static final Logger logger = LoggerFactory.getLogger(MsgQueueMsgTest.class);
    private static List<String> channels = new ArrayList();
    public static void main(String[] args) {
        channels = ConsulUtil.getServiceChannel();
        redisMsgTest();
    }

    public static void redisMsgTest(){
        String queue = ConfigUtil.get("bus.msg.queue");
        JSONObject msg = new JSONObject();
        msg.put("msg", "test push msg: " + ThreadLocalRandom.current().nextInt(10000));
        channels.forEach(item -> {
            msg.put("requestCode", item);//测试页面的发送标识
            JedisUtils.lpush(queue, msg.toJSONString());
            logger.info("lpush bus_queue msg: {}", msg);
        });
    }

    public static void rabbitmqMsgTest(){
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(ConfigUtil.get("rabbitmq.host"));
            factory.setPort(ConfigUtil.getInteger("rabbitmq.port"));
            factory.setUsername(ConfigUtil.get("rabbitmq.user"));
            factory.setPassword(ConfigUtil.get("rabbitmq.passwd"));
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(ConfigUtil.get("rabbitmq.exchange.faout"), BuiltinExchangeType.FANOUT);
            logger.info("rabbitmq message send...");

            for (int i = 0; i < 5 ; i++) {
                JSONObject msg = new JSONObject();
                msg.put("msg", "test push msg: " + ThreadLocalRandom.current().nextInt(10000));
                for (int j = 0; j < channels.size(); j++) {
                    msg.put("requestCode", channels.get(j));//测试页面的发送标识
                    channel.basicPublish(ConfigUtil.get("rabbitmq.exchange.faout"),
                            ConfigUtil.get("rabbitmq.bus.route_key"),
                            null, msg.toJSONString().getBytes());
                    logger.info("rabbitmq bus_queue msg: {}", msg);
                }
                Thread.sleep(1000);
            }
        }catch (Exception e){
            logger.error("rabbitmq message queue listener start failed: {}", e);
        }
    }
}
