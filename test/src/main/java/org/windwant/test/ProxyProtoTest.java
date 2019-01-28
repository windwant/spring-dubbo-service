package org.windwant.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.ConfigUtil;
import org.windwant.protocal.DubboServicePro;
import org.windwant.test.util.RequestResponseUtil;

/**
 * ProxyProtoTest
 * 测试 spring-dubbo-proxy rpc protobuf
 */
public class ProxyProtoTest {

    public static void main(String[] args) {
        new ProxyProtoTest().connect(ConfigUtil.getInteger("server.port"), ConfigUtil.get("server.host"));
    }

    public void connect(int port, String host){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new childHandler());

            ChannelFuture cf = b.connect(host, port).sync();
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    private class childHandler extends ChannelInitializer<SocketChannel> {

        private static final String label = "$";
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new IdleStateHandler(10, 10, 10));
            ch.pipeline().addLast(new HeartBeatProxyHandler());
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
            ch.pipeline().addLast(new ProtobufDecoder(DubboServicePro.TimeResponse.getDefaultInstance()));
            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
            ch.pipeline().addLast(new ProtobufEncoder());
            ch.pipeline().addLast(new ProxyProtoHandler());
        }
    }
}

class ProxyProtoHandler extends ChannelInboundHandlerAdapter{
    private static final Logger logger = LoggerFactory.getLogger(ProxyProtoTest.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DubboServicePro.DubboRequest request = RequestResponseUtil.getDubboRequest(1);
        ctx.writeAndFlush(request);
        logger.info("send request: {}", request.toString());
        request = RequestResponseUtil.getDubboRequest(2);
        ctx.writeAndFlush(request);
        logger.info("send request: {}", request.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("receive server response: [ {} ]", msg);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

class HeartBeatProxyHandler extends ChannelInboundHandlerAdapter {
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