package org.windwant.proxy.bootwsproxy;

import com.google.protobuf.ExtensionRegistry;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.windwant.common.util.ConfigUtil;
import org.windwant.protobuf.BootRequestResponse;
import org.windwant.proxy.BootProxyHandler;

/**
 * Created by Administrator on 2018/2/7.
 */
public class BootProxyInitializer extends ChannelInitializer<SocketChannel> {
    private static ExtensionRegistry registry;

    public BootProxyInitializer() {
        registry = ExtensionRegistry.newInstance();
        BootRequestResponse.registerAllExtensions(registry);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new HttpServerCodec());
        //pipeline.addLast(new HttpRequestDecoderTest());
        pipeline.addLast(new HttpObjectAggregator(65536));//它负责把多个HttpMessage组装成一个完整的Http请求或者响应。到底是组装成请求还是响应，则取决于它所处理的内容是请求的内容，还是响应的内容
        //pipeline.addLast(new HttpResponseEncoderTest());
        pipeline.addLast(new WebSocketServerProtocolHandler(ConfigUtil.get("acceptor.connect.path"), null, true, 1024 * 1024));//允许最大64k的请求数据

        //拆包
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(BootRequestResponse.BootRequest.getDefaultInstance(), registry));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        //业务处理
        pipeline.addLast(new BootProxyHandler());
    }
}
