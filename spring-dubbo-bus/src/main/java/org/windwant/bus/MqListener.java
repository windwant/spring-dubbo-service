package org.windwant.bus;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.bus.util.JedisUtils;
import org.windwant.common.util.ConfigUtil;
import org.windwant.protocal.DubboServicePro;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 消息处理
 * Created by Administrator on 18-4-11.
 */
public class MqListener {

    private static final Logger logger = LoggerFactory.getLogger(MqListener.class);

    public void start(){
        try {
            logger.info("message queue listener start...");
            while (!Thread.currentThread().isInterrupted()) {
                Jedis jedis = JedisUtils.getJedis();
                List<String> msg = jedis.brpop(2000, ConfigUtil.get("bus.websocket.msg_queue"));
                if (msg.isEmpty() || msg.size() == 0) continue;

                handleMsg(msg.get(1));
            }
        }catch (Exception e){
            logger.error("message queue listener start failed: {}", e.getMessage());
        }
    }

    /**
     * 处理json格式msg
     * {
     *     requestCode: BaseRequest,
     *     msg: "test",
     *     status: "0"
     * }
     * @param msg
     */
    private void handleMsg(String msg){
        logger.info("begin handle msg : {}", msg);
        if(BusServerChannelMgr.websocketChannel.size() != 0){
            JSONObject push = JSONObject.parseObject(msg);

            DubboServicePro.DubboResponse response = DubboServicePro.DubboResponse.newBuilder()
                    .setResponseCode(DubboServicePro.DubboResponse.ResponseCode.values()[push.getInteger("requestCode") - 1])
                    .setMsg(push.getString("msg"))
                    .setStatus(0)
                    .build();
            BusServerChannelMgr.websocketChannel.values().stream().forEach(channel -> {
                channel.writeAndFlush(Unpooled.wrappedBuffer(response.toByteArray()))
                        .addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                logger.info("write msg to channel result: {}", future.isSuccess() ? "success" : "failed");
                            }
                        });
            });
        }
    }
}
