package org.windwant.bus;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * netty 通道 handler
 */
public class BusServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender()); //protobuf 处理

        //自定义业务handler
        pipeline.addLast(new BusServerFrameHandler());
    }
}
