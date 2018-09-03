package org.windwant.test;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.util.ConfigUtil;
import org.windwant.test.util.ConsulUtil;
import org.windwant.test.util.JedisUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 获取consul 当前连接通道信息，
 * 向redis 推送队列添加每一个通道需要推送的json消息 测试长连接消息推送
 * Created by Administrator on 18-4-11.
 */
public class PushMsgRedisTest {
    private static final Logger logger = LoggerFactory.getLogger(PushMsgRedisTest.class);
    public static void main(String[] args) {
        String queue = ConfigUtil.get("bus.msg.queue");
        JSONObject msg = new JSONObject();
        List<String> channels = ConsulUtil.getServiceChannel();
        channels.forEach(item -> {
            msg.put("requestCode", item);//测试页面的发送标识
            msg.put("msg", "test push msg: " + ThreadLocalRandom.current().nextInt(10000));
            JedisUtils.lpush(queue, msg.toJSONString());
            logger.info("lpush msg_push msg: {}", msg);
        });
    }
}
