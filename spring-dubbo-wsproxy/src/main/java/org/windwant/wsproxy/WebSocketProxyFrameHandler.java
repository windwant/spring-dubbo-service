package org.windwant.wsproxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.dubbo.DubboSvr;
import org.windwant.wsproxy.util.ConsulUtil;
import org.windwant.wsproxy.util.WSUtil;
import org.windwant.protobuf.BootRequestResponse.BootRequest;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket的binaryFrame的处理，解析protobuf基类，获取路由信息，并异步调用业务路由服务或位置上传服务；对每条通道进行维持与管理
 *
 */
public class WebSocketProxyFrameHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketProxyFrameHandler.class);

    private String requestCode; //连接长连接服务器的用户ID

    private Long time;//客户端连接上长连接服务器的时间戳

    private WebSocketServerHandshaker webSocketServerHandshaker;

    public WebSocketProxyFrameHandler() {
    }
    
    private static final ExecutorService workerExecutorService = newBlockingExecutors(Runtime.getRuntime().availableProcessors() * 2);
    
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
    protected void channelRead0(ChannelHandlerContext context, Object webSocketFrame) throws Exception {
        if (webSocketFrame instanceof BinaryWebSocketFrame) {
            //Binary --> ByteBuf 读取消息
            ByteBuf inMessageBytes = ((WebSocketFrame)webSocketFrame).content();
            byte[] inBytes = new byte[inMessageBytes.readableBytes()];
            while (inMessageBytes.isReadable()) {
                inMessageBytes.readBytes(inBytes);
            }
            BootRequest bootRequest = null;
            try {
            	bootRequest = BootRequest.parseFrom(inBytes);
            }catch (Exception e) {
            	// 当出现异常记录异常,中断连接，在移动网络中，protobuf数据很有可能发生损坏，但protobuf并不对数据内容做校验，只做格式校验，一旦出错，就会抛异常
            	logger.warn("the request protobuf data is corrupted!");
                return;
            }
            requestCode = String.valueOf(bootRequest.getRequestCode());
            //对baseRequest解析，
            logger.info("request requestCode:{}", requestCode);

            //本地路由为空时，其他方法执行时维护用户路由信息
            if (WebSocketProxyChannelManager.getUserChannel(requestCode) == null) {
                WebSocketProxyChannelManager.registerUserChannel(requestCode, context.channel());
                ConsulUtil.putRequestChannel(requestCode);
            }
            WebSocketBusiHandler.dealBusi(context, bootRequest, DubboSvr.dubboService);
        } else if(webSocketFrame instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) webSocketFrame;
            if(!req.decoderResult().isSuccess()
                    || !"websocket".equals(req.headers().get("upgrade"))){
                sendHttpResponse(context, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
                return;
            }

            WebSocketServerHandshakerFactory wf = new WebSocketServerHandshakerFactory("ws://localhost:8087/websocket", null, false);
            webSocketServerHandshaker = wf.newHandshaker(req);
            if(webSocketServerHandshaker == null){
                wf.sendUnsupportedVersionResponse(context.channel());
            }else{
                webSocketServerHandshaker.handshake(context.channel(), req);
            }
        }else {
        	logger.error(" websocket data valid failed, not BinaryWebSocketFrame!");
            WSUtil.response(context.channel(), Integer.parseInt(requestCode), -1, "not supported");
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse resp){
        if(resp.status().code() != 200){
            ByteBuf bf = Unpooled.copiedBuffer(resp.getStatus().toString(), CharsetUtil.UTF_8);
            resp.content().writeBytes(bf);
            bf.release();
            HttpUtil.setContentLength(resp, resp.content().readableBytes());
        }

        ChannelFuture cf = ctx.writeAndFlush(resp);
        if(HttpUtil.isKeepAlive(req) || resp.status().code() != 200){
            cf.addListener(ChannelFutureListener.CLOSE);
        }


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        time = new Date().getTime();
        String key = time + "_" + ctx.channel().id().asLongText();
    }

    //关闭成功后收到通知的事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if(requestCode != null) {
            logger.info("requestCode: {} channel is inactive!", requestCode);
            String key = time + "_" + ctx.channel().id().asLongText();
            //清理本地map中该用户的channel
            WSUtil.removeChannel(requestCode, ctx.channel());

        }
        //关闭与用户通道
        if (ctx.channel() != null) {
        	ctx.channel().close();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        
        logger.error("websocket exceptionCaught: {}", cause);
        WSUtil.removeChannel(requestCode, null);
        //发送错误消息，关闭通道
        WSUtil.response(ctx.channel(), Integer.parseInt(requestCode), -1, "not supported");
    }
}
