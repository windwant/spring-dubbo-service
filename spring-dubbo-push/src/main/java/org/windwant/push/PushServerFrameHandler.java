package org.windwant.push;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.protobuf.BootRequestResponse;
import org.windwant.protobuf.BootRequestResponse.BootRequest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PushServerFrameHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(PushServerFrameHandler.class);

    private static final ExecutorService workerExecutorService = newBlockingExecutors(Runtime.getRuntime().availableProcessors() * 2);

    /**
     * 构造线程池
     */
    private static ExecutorService newBlockingExecutors(int size) {
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100),
                (r, executor) -> {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        logger.error("newBlockingExecutors put error", e);
                    }
                });
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object content) throws Exception {
        try {
            ByteBuf inMessageBytes = (ByteBuf) content;
            byte[] inBytes = new byte[inMessageBytes.readableBytes()];
            while (inMessageBytes.isReadable()) {
                inMessageBytes.readBytes(inBytes);
            }
            BootRequest bootRequest = null;
            try {
                bootRequest = BootRequestResponse.BootRequest.parseFrom(inBytes);
            }catch (Exception e) {
                logger.warn("data error!");
                return;
            }
            String requestCode = String.valueOf(bootRequest.getRequestCode());
            
            logger.info("requestCode {}, request {}", requestCode, bootRequest.toString());
        }  catch (Exception e) {
        	logger.error(e.toString());
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        PushServerChannelMgr.websocketChannel = ctx.channel();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
