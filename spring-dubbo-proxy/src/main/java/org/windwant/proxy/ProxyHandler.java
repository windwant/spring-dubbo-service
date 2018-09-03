package org.windwant.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.protocal.DubboServicePro;

/**
 * Created by Administrator on 2018/2/7.
 */
public class ProxyHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ProxyHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("proxy received msg: {}", msg);
        ByteBuf inMessageBytes = (ByteBuf) msg;
        byte[] inBytes = null;
        try {
            inBytes = new byte[inMessageBytes.readableBytes()];
            while (inMessageBytes.isReadable()) {
                inMessageBytes.readBytes(inBytes);
            }
        } catch (Exception e) {
            logger.error("request msg parsed failed", e);
            return;
        } finally {
            inMessageBytes.release();
        }

        try {
            DubboServicePro.DubboRequest dubboRequest =  DubboServicePro.DubboRequest.parseFrom(inBytes);
            ProxyBusiHandler.getBusiResponse(dubboRequest, ctx);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
