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
import org.windwant.util.NetUtil;
import org.windwant.protocal.DubboServicePro;
import org.windwant.wsproxy.util.ConsulUtil;
import org.windwant.wsproxy.util.WSUtil;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 处理WebSocket binaryFrame；
 * 解析protobuf消息；
 * 通道管理与维护
 */
public class WSProxyFrameHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WSProxyFrameHandler.class);

    private String requestCode; //连接长连接服务器的标识

    private Long time;//客户端连接上长连接服务器的时间戳

    private WebSocketServerHandshaker webSocketServerHandshaker;

    public WSProxyFrameHandler() {
    }
    
    private static final ExecutorService workerExecutorService = newBlockingExecutors(Runtime.getRuntime().availableProcessors() * 2);
    
    private static ExecutorService newBlockingExecutors(int size) {
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100),
                (r, executor) -> {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        logger.error("task reput queue put failed", e);
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
            //protobuf 消息解析
            DubboServicePro.DubboRequest dubboRequest = null;
            try {
                dubboRequest = DubboServicePro.DubboRequest.parseFrom(inBytes);
            }catch (Exception e) {
            	logger.warn("request protobuf data parsed failed!");
                return;
            }
            requestCode = String.valueOf(dubboRequest.getRequestCode().getNumber());
            //对baseRequest解析，
            logger.info("websocket request requestCode:{}", dubboRequest.getRequestCode());

            //channel维护管理
            if (WSProxyChannelManager.getUserChannel("channel-" + requestCode) == null) {
                WSProxyChannelManager.registerUserChannel("channel-" + requestCode, context.channel());
                ConsulUtil.putRequestChannel(NetUtil.getHost(), requestCode);
            }
            //处理业务逻辑
            WSBusiHandler.dealBusi(context, dubboRequest);
        } else if(webSocketFrame instanceof FullHttpRequest) {//初始连接
            FullHttpRequest req = (FullHttpRequest) webSocketFrame;
            //非websocket连接
            if(!req.decoderResult().isSuccess()
                    || !"websocket".equals(req.headers().get("upgrade"))){
                sendHttpResponse(context, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
                return;
            }

            //握手连接
            WebSocketServerHandshakerFactory wf = new WebSocketServerHandshakerFactory("ws://localhost:8087/websocket", null, false);
            webSocketServerHandshaker = wf.newHandshaker(req);
            if(webSocketServerHandshaker == null){
                wf.sendUnsupportedVersionResponse(context.channel());
            }else{
                webSocketServerHandshaker.handshake(context.channel(), req);
            }
        }else {
        	logger.error(" websocket data valid failed, not BinaryWebSocketFrame!");
            WSUtil.responseFailed(context.channel(), DubboServicePro.DubboResponse.ResponseCode.BaseResponse, -1, "not supported");
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

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if(requestCode != null) {
            logger.info("requestCode: {} channel is inactive!", requestCode);
            String key = time + "_" + ctx.channel().id().asLongText();
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
        WSUtil.responseFailed(ctx.channel(), DubboServicePro.DubboResponse.ResponseCode.BaseResponse, -1, "not supported");
    }
}
