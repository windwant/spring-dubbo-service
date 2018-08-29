package org.windwant.bus;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.protocal.BootRequestResponse;
import org.windwant.bus.util.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 18-4-11.
 */
public class MqMonitor {

    private static final Logger logger = LoggerFactory.getLogger(MqMonitor.class);

    private static final String PUSH_KEY = "msg_push";

    public void start(){
        try {
            logger.info("mq monitor start...");
            while (!Thread.currentThread().isInterrupted()) {
                Jedis jedis = JedisUtils.getJedis();
                List<String> msg = jedis.brpop(2000, PUSH_KEY);
                if (msg.isEmpty() || msg.size() == 0) continue;

                handleMsg(msg.get(1));
            }
        }catch (Exception e){
            logger.error("mq monitor error: {}", e.getMessage());
        }
    }

    /**
     * 处理json格式msg
     * {
     *     requestCode: 0,
     *     msg: "test"
     * }
     * @param msg
     */
    private void handleMsg(String msg){
        logger.info("begin hanle msg : {}", msg);
        if(BusServerChannelMgr.websocketChannel != null){
            JSONObject push = JSONObject.parseObject(msg);

            BootRequestResponse.BootResponse response = BootRequestResponse.BootResponse.newBuilder()
                    .setRequestCode(push.getInteger("requestCode"))
                    .setRespCode(push.getInteger("requestCode"))
                    .setResult(push.getString("msg"))
                    .build();
            BusServerChannelMgr.websocketChannel.writeAndFlush(Unpooled.wrappedBuffer(response.toByteArray()))
            .addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.info("push result: {}", future.isSuccess()?"success":"failed");
                }
            });
        }
    }
}
