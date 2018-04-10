package org.windwant.wsproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.util.ConfigUtil;
import org.windwant.registry.RegistryFactory;
import org.windwant.registry.RegistryService;

/**
 * WSProxy server
 *
 */
public class WebSocketProxyServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketProxyServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private String serviceId = null;

    public static void main(String[] args) throws Exception {
        WebSocketProxyServer wss = new WebSocketProxyServer();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                wss.shutdownGraceFully();
            }
        });
        wss.start();
    }
    
    private void start() throws InterruptedException {
        logger.info("WSProxy Starting...");
        bossGroup = new NioEventLoopGroup(2);

        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap;
         ChannelFuture channelFuture = null;
         ChannelFuture channelFutureT = null;
         try {
             bootstrap = new ServerBootstrap();
             bootstrap.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     //如果要求高实时性，有数据就马上发送，该选项设置为true关闭Nagle算法
                     //如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false
                     .option(ChannelOption.TCP_NODELAY, true)
                     .option(ChannelOption.SO_RCVBUF, 256 * 1024)//接收缓冲
                     .option(ChannelOption.SO_SNDBUF, 256 * 1024)//发送缓冲
                     //当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                     .option(ChannelOption.SO_BACKLOG, 8192)
                     .childHandler(new WebSocketProxyInitializer());
             channelFuture = bootstrap.bind(ConfigUtil.getInteger("websocket.connect.port"));
             //向consul注册服务
             serviceId = RegistryFactory.INSTANCE.
                     getRegistry(RegistryFactory.CONSUL).
                     doRegister(RegistryService.build("websocket",
                             "localhost",
                             ConfigUtil.getInteger("websocket.connect.port"),
                             "1.0"));
         } catch (Exception e) {
             logger.error("Acceptor Server Start ERROR", e);
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
    		logger.info("the boss group is shutdown gracefully!");
    		bossGroup.shutdownGracefully();
    	}
    	if (workerGroup != null) {
    		logger.info("the work group is shutdown gracefully!");
    		workerGroup.shutdownGracefully();
    	}
        if(serviceId != null) {
            RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).doUnRegister(serviceId);
        }
     }


}
