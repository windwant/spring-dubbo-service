package org.windwant.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.util.ConfigUtil;
import org.windwant.common.util.DateUtil;
import org.windwant.protobuf.BootRequestResponse;

/**
 * BootProtoClient
 * 测试 rpc protobuf
 */
public class BootProtoClient {

    public static void main(String[] args) {
        new BootProtoClient().connect(ConfigUtil.getInteger("server.port"), ConfigUtil.get("server.host"));
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
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
            ch.pipeline().addLast(new ProtobufDecoder(BootRequestResponse.BootResponse.getDefaultInstance()));
            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
            ch.pipeline().addLast(new ProtobufEncoder());
            ch.pipeline().addLast(new BootClientProtoHandler());
        }
    }
}

class BootClientProtoHandler extends ChannelInboundHandlerAdapter{
    private static final Logger logger = LoggerFactory.getLogger(BootProtoClient.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            BootRequestResponse.BootRequest request = getBootRequest(i);
            ctx.writeAndFlush(request);
            Thread.sleep(1000);
            logger.info("send request: {}", request.toString());
        }
    }

    private BootRequestResponse.BootRequest getBootRequest(int id){
        BootRequestResponse.BootRequest.Builder builder = BootRequestResponse.BootRequest.newBuilder();
        builder.setRequestCode(id);
        builder.setName(String.valueOf(id));
        builder.setSex(1);
        builder.setAccessTime(DateUtil.getCurrentTime());
        return builder.build();
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
