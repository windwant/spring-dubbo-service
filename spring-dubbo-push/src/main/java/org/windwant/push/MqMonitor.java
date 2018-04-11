package org.windwant.push;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.protobuf.BootRequestResponse;
import org.windwant.push.util.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 18-4-11.
 */
public class MqMonitor {

    private static final Logger logger = LoggerFactory.getLogger(MqMonitor.class);

    private static final String PUSH_KEY = "msg_push";

    public void start(){
        logger.info("mq monitor start...");
        while (!Thread.currentThread().isInterrupted()){
            Jedis jedis = JedisUtils.getJedis();
            List<String> msg = jedis.brpop(2000, PUSH_KEY);
            if(msg.isEmpty() || msg.size() == 0) continue;

            handleMsg(msg.get(1));
        }
    }

    private void handleMsg(String msg){
        logger.info("begin hanle msg : {}", msg);
        if(PushServerChannelMgr.websocketChannel != null){
            BootRequestResponse.BootResponse response = BootRequestResponse.BootResponse.newBuilder()
                    .setRequestCode(Integer.parseInt(msg))
                    .setRespCode(Integer.parseInt(msg))
                    .setResult("push msg  test")
                    .build();
            PushServerChannelMgr.websocketChannel.writeAndFlush(Unpooled.wrappedBuffer(response.toByteArray()))
            .addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.info("push result: {}", future.isSuccess()?"success":"failed");
                }
            });
        }
    }
}
