package org.windwant.wsproxy;

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
import io.netty.handler.stream.ChunkedWriteHandler;
import org.windwant.protocal.BootRequestResponse;

/**
 * Netty的channel ChannelInboundHandlerAdapter 初始handler
 */
public class WSProxyInitializer extends ChannelInitializer<SocketChannel> {

    private static ExtensionRegistry registry;

    public WSProxyInitializer() {
        registry = ExtensionRegistry.newInstance();
        BootRequestResponse.registerAllExtensions(registry);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

//        pipeline.addLast("http-codec", new HttpServerCodec());
//        pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
//        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
//        pipeline.addLast(new WebSocketServerHandler());
//
        pipeline.addLast(new HttpServerCodec());//http协议进行编解码
        pipeline.addLast(new HttpObjectAggregator(65536));//它负责把多个HttpMessage组装成一个完整的Http请求或者响应。到底是组装成请求还是响应，则取决于它所处理的内容是请求的内容，还是响应的内容
        pipeline.addLast(new ChunkedWriteHandler());//分块写处理
        pipeline.addLast(new ProtobufVarint32FrameDecoder()); //长度解码
        pipeline.addLast(new ProtobufDecoder(BootRequestResponse.BootRequest.getDefaultInstance(), registry)); //protobuf 解码
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());//添加长度
        pipeline.addLast(new ProtobufEncoder()); //protobuf 编码
        pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));
        pipeline.addLast(new WSProxyFrameHandler());
    }
}
