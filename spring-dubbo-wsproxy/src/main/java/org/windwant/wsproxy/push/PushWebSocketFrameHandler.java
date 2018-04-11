package org.windwant.wsproxy.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.protobuf.BootRequestResponse;
import org.windwant.wsproxy.WebSocketProxyChannelManager;
import org.windwant.wsproxy.util.WSUtil;

/**
 * 推送
 */
public class PushWebSocketFrameHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(PushWebSocketFrameHandler.class);

    public PushWebSocketFrameHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Object object) throws Exception {
        try{
            ByteBuf inMessageBytes = (ByteBuf) object;
            byte[] inBytes = new byte[inMessageBytes.readableBytes()];
            while (inMessageBytes.isReadable()) {
                inMessageBytes.readBytes(inBytes);
            }
            BootRequestResponse.BootResponse response = null;
            try {
                response = BootRequestResponse.BootResponse.parseFrom(inBytes);
            }catch (Exception e) {
                logger.warn("the request protobuf data is corrupted!");
                return;
            }
            String requestCode = String.valueOf(response.getRequestCode());
            logger.info("request requestCode:{}", requestCode);

            if (WebSocketProxyChannelManager.getUserChannel(requestCode) == null) {
                WebSocketProxyChannelManager.registerUserChannel(requestCode, context.channel());

            }
            Channel channel = WebSocketProxyChannelManager.getUserChannel("channel-" + requestCode);

            if(channel==null || !channel.isActive()){
                logger.info("received push server send message ! {} channel is unavaliable!", requestCode);
                WSUtil.response(context.channel(), response.getRequestCode(), response.getRequestCode(), "channel is unavaliable");
                return;
            }

            ByteBuf outMessageBytes = Unpooled.buffer(inBytes.length);
            outMessageBytes.writeBytes(inBytes);
            //response push
            channel.writeAndFlush(new BinaryWebSocketFrame(outMessageBytes)).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        WSUtil.response(context.channel(), Integer.parseInt(requestCode),  Integer.parseInt(requestCode), "message push failed");
                        logger.info("requestCode: {}, send push message failed !", requestCode);
                    }else
                        context.channel().read();
                }
            });
        }catch (Exception e){
            logger.error(e.toString());
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String ip = ctx.channel().remoteAddress().toString();
        //0是心跳连接
        if (!"0".equals(ip) && WebSocketProxyChannelManager.getPushChannel(ip)==null) {
            WebSocketProxyChannelManager.registerPushChannel(ip, ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        WebSocketProxyChannelManager.registerPushChannel(ctx.channel().remoteAddress().toString(), ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 当出现异常就关闭连接
        logger.error("push websocket handler exception ERROR", cause);
        WebSocketProxyChannelManager.registerPushChannel(ctx.channel().remoteAddress().toString(), ctx.channel());
        ctx.close();
    }

}
