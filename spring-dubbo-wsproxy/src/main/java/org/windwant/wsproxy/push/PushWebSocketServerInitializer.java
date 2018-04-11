package org.windwant.wsproxy.push;

import com.google.protobuf.ExtensionRegistry;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.windwant.protobuf.BootRequestResponse;

/**
 */
public class PushWebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    public static ExtensionRegistry registry;

    public PushWebSocketServerInitializer() {
        registry = ExtensionRegistry.newInstance();
        BootRequestResponse.registerAllExtensions(registry);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //解决tcp拆包问题
        pipeline.addLast(new ProtobufVarint32FrameDecoder());

        pipeline.addLast(new PushWebSocketFrameHandler());
    }
}
