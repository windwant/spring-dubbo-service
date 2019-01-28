package org.windwant.rpcproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.ConfigUtil;

/**
 * Hello world!
 *
 */
public class RpcProxyServer
{
    private static final Logger logger = LoggerFactory.getLogger(RpcProxyServer.class);

    private EventLoopGroup connectGroup;
    private EventLoopGroup workGroup;

    public static void main(String[] args) throws Exception {
        RpcProxyServer bootProxy = new RpcProxyServer();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                bootProxy.shutdownGraceFully();
            }
        });
        bootProxy.start();
    }


    private void start() throws InterruptedException {
        logger.info("rpc proxy server start ... ");
        connectGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap;
        ChannelFuture channelFuture = null;
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(connectGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_RCVBUF, 256 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 256 * 1024)
                    .childHandler(new RpcProxyInitializer());
            //绑定端口
            channelFuture = bootstrap.bind(ConfigUtil.getInteger("server.port"));
        } catch (Exception e) {
            logger.error("rpc proxy server start failed", e);
            throw new RuntimeException(e);
        } finally {
            if (null != channelFuture) {
                channelFuture.sync().channel().closeFuture().sync();
            }
            connectGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public void shutdownGraceFully() {
        if (connectGroup != null) {
            logger.info("connectGroup shutdown gracefully!");
            connectGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            logger.info("workGroup shutdown gracefully!");
            workGroup.shutdownGracefully();
        }
    }
}
