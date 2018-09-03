package org.windwant.wsproxy.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.protocal.DubboServicePro;
import org.windwant.wsproxy.WSProxyChannelManager;
import org.windwant.wsproxy.util.WSUtil;

/**
 * 消息处理
 */
public class BusWSFrameHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(BusWSFrameHandler.class);

    public BusWSFrameHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Object object) throws Exception {
        try{
            ByteBuf inMessageBytes = (ByteBuf) object;
            byte[] inBytes = new byte[inMessageBytes.readableBytes()];
            while (inMessageBytes.isReadable()) {
                inMessageBytes.readBytes(inBytes);
            }
            DubboServicePro.DubboResponse response = null;
            try {
                response = DubboServicePro.DubboResponse.parseFrom(inBytes);
            }catch (Exception e) {
                logger.warn("request protobuf data parsed failed!");
                return;
            }
            String requestCode = String.valueOf(response.getResponseCode().getNumber());
            logger.info("request requestCode:{}", response.getResponseCode());

            //注册用户通道
            if (WSProxyChannelManager.getUserChannel(requestCode) == null) {
                WSProxyChannelManager.registerUserChannel(requestCode, context.channel());

            }
            Channel channel = WSProxyChannelManager.getUserChannel("channel-" + requestCode);

            if(channel==null || !channel.isActive()){
                logger.info("received bus server send message ! {} channel is unavaliable!", requestCode);
                WSUtil.responseFailed(context.channel(), response.getResponseCode(), -1, "channel is unavaliable");
                return;
            }

            ByteBuf outMessageBytes = Unpooled.buffer(inBytes.length);
            outMessageBytes.writeBytes(inBytes);
            //response bus message
            channel.writeAndFlush(new BinaryWebSocketFrame(outMessageBytes)).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        WSUtil.responseFailed(context.channel(), DubboServicePro.DubboResponse.ResponseCode.BaseResponse, -1, "message push failed");
                        logger.info("requestCode: {}, send bus message failed !", requestCode);
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
        if (!"0".equals(ip) && WSProxyChannelManager.getBusChannel(ip)==null) {
            WSProxyChannelManager.registerBusChannel(ip, ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        WSProxyChannelManager.removeBusChannel(ctx.channel().remoteAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 当出现异常就关闭连接
        logger.error("bus websocket handler exception failed", cause);
        WSProxyChannelManager.removeBusChannel(ctx.channel().remoteAddress().toString());
        ctx.close();
    }

}
