package org.windwant.wsproxy.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.springframework.util.StringUtils;
import org.windwant.protobuf.BootRequestResponse;
import org.windwant.wsproxy.BootWSProxyChannelManager;

/**
 */
public class WSUtil {

    /**
     * 通道移除，直接关闭通道
     */
    public static void removeChannel(String requestCode, Channel channel) {
        if (!StringUtils.isEmpty(requestCode)) {
            //requestCode->Channel 关系移除
            BootWSProxyChannelManager.removeUserChannel(requestCode);
        }

        if (null != channel && channel.isActive()) {
            //客户端不应该给长连接服务发送空消息
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            channel.close();
        }
    }


    /**
     * 只发送错误消息
     */
    public static void response(Channel channel, int requestCode, int respCode, String message) {
        if (null != channel && channel.isActive()) {
            channel.writeAndFlush(getBinaryWebSocketFrameResponse(requestCode, respCode, message));
        }
    }

    /**
     * 发送错误消息并关闭通道
     */
    public static void responseThenClose(Channel channel, int requestCode, int respCode, String message) {
        if (null != channel && channel.isActive()) {
            channel.writeAndFlush(getBinaryWebSocketFrameResponse(requestCode, respCode, message)).addListener(channelFuture -> channel.close());
        }
    }

    protected static ByteBuf getByteBufResponse(int requestCode, int respCode, String message){
        BootRequestResponse.BootResponse.Builder builder = BootRequestResponse.BootResponse.newBuilder();
        builder.setRequestCode(requestCode).setRespCode(respCode).setResult(message);
        return Unpooled.wrappedBuffer(builder.build().toByteArray());
    }

    public static BinaryWebSocketFrame getBinaryWebSocketFrameResponse(int requestCode, int respCode, String message){
        return new BinaryWebSocketFrame(getByteBufResponse(requestCode, respCode, message));
    }
}
