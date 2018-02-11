package org.windwant.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/2/11.
 */
public class HeartBeatProxyHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatProxyHandler.class);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                logger.info("idle event: {}", IdleState.READER_IDLE);
            }else if(state == IdleState.WRITER_IDLE){
                logger.info("idle event: {}", IdleState.WRITER_IDLE);
            }else if(state == IdleState.ALL_IDLE){
                logger.info("idle event: {}", IdleState.ALL_IDLE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
