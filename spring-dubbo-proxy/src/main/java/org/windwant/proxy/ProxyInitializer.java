package org.windwant.proxy;

import com.google.protobuf.ExtensionRegistry;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.windwant.protocal.DubboServicePro;

/**
 * Created by Administrator on 2018/2/7.
 */
public class ProxyInitializer extends ChannelInitializer<SocketChannel> {
    private static ExtensionRegistry registry;

    public ProxyInitializer() {
        registry = ExtensionRegistry.newInstance();
        DubboServicePro.registerAllExtensions(registry);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(10, 10, 10));
        pipeline.addLast(new HeartBeatProxyHandler());
        //拆包
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        //业务处理
        pipeline.addLast(new ProxyHandler());

    }
}
