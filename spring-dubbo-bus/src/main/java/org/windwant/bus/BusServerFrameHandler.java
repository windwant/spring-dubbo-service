package org.windwant.bus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.protocal.DubboServicePro;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 业务处理
 */
public class BusServerFrameHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(BusServerFrameHandler.class);

    private static final ExecutorService workerExecutorService = newBlockingExecutors(Runtime.getRuntime().availableProcessors() * 2);

    /**
     * 构造独立业务处理线程池
     */
    private static ExecutorService newBlockingExecutors(int size) {
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100),
                (r, executor) -> {
                    try {
                        //将被丢弃的任务重新放入队列
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        logger.error("task reput queue failed", e);
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
            //解析请求 基本请求类型消息
            DubboServicePro.DubboRequest request = null;
            try {
                request = DubboServicePro.DubboRequest.parseFrom(inBytes);
            }catch (Exception e) {
                logger.warn("protobuf parse failed !");
                return;
            }
            String requestCode = String.valueOf(request.getRequestCode());
            
            logger.info("requestCode {}, request {}", requestCode, request.toString());
        }  catch (Exception e) {
        	logger.error(e.toString());
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        BusServerChannelMgr.websocketChannel.put(ctx.name(), ctx.channel());
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
