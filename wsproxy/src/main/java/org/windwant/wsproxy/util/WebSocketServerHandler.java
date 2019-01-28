package org.windwant.wsproxy.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2018/2/9.
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());

    private WebSocketServerHandshaker webSocketServerHandshaker;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req){
        if(!req.decoderResult().isSuccess()
                || !"websocket".equals(req.headers().get("upgrade"))){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wf = new WebSocketServerHandshakerFactory("ws://localhost:8888/websocket", null, false);
        webSocketServerHandshaker = wf.newHandshaker(req);
        if(webSocketServerHandshaker == null){
            wf.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        }else{
            webSocketServerHandshaker.handshake(ctx.channel(), req);
        }

    }

    private void handleWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        if(frame instanceof CloseWebSocketFrame){
            webSocketServerHandshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if(!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException();
        }

        String request = ((TextWebSocketFrame) frame).text();
        System.out.println(ctx.channel() + " receive: " + request);

        //ctx.write();
        ctx.channel().write(new TextWebSocketFrame(request
                + ", welcome to use websocket, now time is: "
                + new Date().toString()));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse resp){
        if(resp.status().code() != 200){
            ByteBuf bf = Unpooled.copiedBuffer(resp.status().toString(), CharsetUtil.UTF_8);
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
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if (msg instanceof WebSocketFrame){
            handleWebsocketFrame(ctx, (WebSocketFrame) msg);
        }
    }
}