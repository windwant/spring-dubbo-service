package org.windwant.wsproxy.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.windwant.protocal.DubboServicePro;

/**
 */
public class WSUtil {

    public static void responseFailed(Channel channel, DubboServicePro.DubboResponse.ResponseCode type, int status, String msg) {
        if (null != channel && channel.isActive()) {
            DubboServicePro.DubboResponse response = DubboServicePro.DubboResponse.newBuilder().setStatus(status).setMsg(msg).setResponseCode(type).build();
            channel.writeAndFlush(getBinaryWebSocketFrameResponse(response));
        }
    }

    /**
     * 只发送错误消息
     */
    public static void response(Channel channel, DubboServicePro.DubboResponse response) {
        if (null != channel && channel.isActive()) {
            channel.writeAndFlush(getBinaryWebSocketFrameResponse(response));
        }
    }

    /**
     * 发送错误消息并关闭通道
     */
    public static void responseThenClose(Channel channel, DubboServicePro.DubboResponse response) {
        if (null != channel && channel.isActive()) {
            channel.writeAndFlush(getBinaryWebSocketFrameResponse(response)).addListener(channelFuture -> channel.close());
        }
    }

    protected static ByteBuf getByteBufResponse(DubboServicePro.DubboResponse response){
        return Unpooled.wrappedBuffer(response.toByteArray());
    }

    public static BinaryWebSocketFrame getBinaryWebSocketFrameResponse(DubboServicePro.DubboResponse response){
        return new BinaryWebSocketFrame(getByteBufResponse(response));
    }

}
