package org.windwant.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.dubbo.DubboSvr;
import org.windwant.protobuf.BootRequestResponse.BootRequest;

/**
 * Created by Administrator on 2018/2/7.
 */
public class BootProxyHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BootProxyHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("received: {}", msg);
        ByteBuf inMessageBytes = (ByteBuf) msg;
        byte[] inBytes = null;
        try {
            inBytes = new byte[inMessageBytes.readableBytes()];
            while (inMessageBytes.isReadable()) {
                inMessageBytes.readBytes(inBytes);
            }
        } catch (Exception e) {
            logger.error("parse request msg failed", e);
            return;
        } finally {
            inMessageBytes.release();
        }
        BootRequest bootRequest =  BootRequest.parseFrom(inBytes);
        try {
            //业务逻辑处理，可能会引起阻塞
            BusiHandler.getBusiResponse(DubboSvr.dubboService, bootRequest, ctx);
        }catch (Exception e){}
    }
}
