package org.windwant.bus.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.bus.util.JedisUtils;
import org.windwant.util.ConfigUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 消息处理
 * Created by Administrator on 18-4-11.
 */
public class RedisQueueListener implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RedisQueueListener.class);

    public void run(){
        try {
            logger.info("redis message queue listener start...");
            while (!Thread.currentThread().isInterrupted()) {
                Jedis jedis = JedisUtils.getJedis();
                List<String> msg = jedis.brpop(2000, ConfigUtil.get("bus.websocket.msg_queue"));
                if (msg.isEmpty() || msg.size() == 0) continue;

                MqMsgHandler.handleMsg(msg.get(1));
            }
        }catch (Exception e){
            logger.error("redis message queue listener start failed: {}", e.getMessage());
        }
    }
}
