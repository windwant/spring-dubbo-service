package org.windwant.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.util.ConfigUtil;

/**
 * Hello world!
 *
 */
public class ProxyServer
{
    private static final Logger logger = LoggerFactory.getLogger(ProxyServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public static void main(String[] args) throws Exception {
        ProxyServer bootProxy = new ProxyServer();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                bootProxy.shutdownGraceFully();
            }
        });
        bootProxy.start();
    }


    private void start() throws InterruptedException {
        logger.info("proxy server start... ");
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap;
        ChannelFuture channelFuture = null;
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_RCVBUF, 256 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 256 * 1024)
                    .childHandler(new ProxyInitializer());
            //绑定端口
            channelFuture = bootstrap.bind(ConfigUtil.getInteger("server.port"));
        } catch (Exception e) {
            logger.error("proxy Server Start failed", e);
            throw new RuntimeException(e);
        } finally {
            if (null != channelFuture) {
                channelFuture.sync().channel().closeFuture().sync();
            }
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void shutdownGraceFully() {
        if (bossGroup != null) {
            logger.info("boss group shutdown gracefully!");
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            logger.info("work group shutdown gracefully!");
            workerGroup.shutdownGracefully();
        }
    }
}
