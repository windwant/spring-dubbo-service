package org.windwant.bus.mq;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.bus.BusServerChannelMgr;
import org.windwant.protocal.DubboServicePro;

/**
 * Created by Administrator on 18-9-3.
 */
public class MqMsgHandler {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);

    /**
     * 处理json格式msg
     * {
     *     requestCode: BaseRequest,
     *     msg: "test",
     *     status: "0"
     * }
     * @param msg
     */
    public static void handleMsg(String msg){
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
