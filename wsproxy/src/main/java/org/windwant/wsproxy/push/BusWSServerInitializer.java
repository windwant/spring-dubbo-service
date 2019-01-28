package org.windwant.wsproxy.push;

import com.google.protobuf.ExtensionRegistry;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.windwant.protocal.DubboServicePro;

/**
 */
public class BusWSServerInitializer extends ChannelInitializer<SocketChannel> {

    public static ExtensionRegistry registry;

    public BusWSServerInitializer() {
        registry = ExtensionRegistry.newInstance();
        DubboServicePro.registerAllExtensions(registry);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //解决tcp拆包问题
        pipeline.addLast(new ProtobufVarint32FrameDecoder());

        pipeline.addLast(new BusWSFrameHandler());
    }
}
