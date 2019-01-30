package org.windwant.wsproxy.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.protocal.DubboServicePro;
import org.windwant.util.NetUtil;
import org.windwant.wsproxy.WSProxyChannelManager;
import org.windwant.wsproxy.util.ConsulUtil;
import org.windwant.wsproxy.util.WSUtil;

/**
 * 消息处理
 * 向connect channel 写入从 bus channel 读取的消息
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

            //注册消息服务通道 本地缓存 及 consul注册（host_bus/channel-requestCode）
            String channelName = WSProxyChannelManager.mapBusRequestCodeToChannel(requestCode);
            if (WSProxyChannelManager.getBusChannel(channelName) == null) {
                WSProxyChannelManager.registerBusChannel(channelName, context.channel());
                ConsulUtil.putRequestChannel(NetUtil.getHost() + "_bus", requestCode);
            }

            //获取connect channel
            Channel channel = WSProxyChannelManager.getConnectChannel(WSProxyChannelManager.mapRequestCodeToChannel(requestCode));

            if(channel==null || !channel.isActive()){
                logger.info("received bus server send message ! {} channel is unavaliable!", requestCode);
                WSUtil.responseFailed(context.channel(), response.getResponseCode(), -1, "channel is unavaliable");
                return;
            }

            ByteBuf outMessageBytes = Unpooled.buffer(inBytes.length);
            outMessageBytes.writeBytes(inBytes);
            //response bus message  写入从 bus channel 读取的消息
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
