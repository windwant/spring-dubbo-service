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
import org.windwant.common.util.NetUtil;
import org.windwant.registry.RegistryFactory;
import org.windwant.registry.RegistryService;
import org.windwant.wsproxy.push.PushWebSocketServerInitializer;
import org.windwant.wsproxy.util.ConsulUtil;
import org.windwant.wsproxy.util.WSUtil;

/**
 * WSProxy server
 *
 */
public class WebSocketProxyServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketProxyServer.class);

    private EventLoopGroup bossGroup; //主线程组 处理连接
    private EventLoopGroup workerGroup; //工作线程组 处理业务

    private String wsServiceId = null; //连接服务
    private String wsPushServiceId = null; //推送服务

    public static void main(String[] args) throws Exception {
        WebSocketProxyServer wss = new WebSocketProxyServer();
        //优雅停机
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                wss.shutdownGraceFully();
            }
        });
        wss.start();
    }
    
    private void start() throws InterruptedException {
        ConsulUtil.initClear();
        logger.info("WSProxy Starting...");
        bossGroup = new NioEventLoopGroup(2);

        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap; //启动工具
        ChannelFuture channelFuture = null;
        ServerBootstrap pushServerBootStrap;
        ChannelFuture pushChannelFuture = null;
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                            //如果要求高实时性，有数据就马上发送，该选项设置为true关闭Nagle算法
                            //如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_RCVBUF, 256 * 1024)//接收缓冲区
                    .option(ChannelOption.SO_SNDBUF, 256 * 1024)//发送缓冲区
                            //当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 8192)
                    .childHandler(new WebSocketProxyInitializer());
            channelFuture = bootstrap.bind(ConfigUtil.getInteger("websocket.connect.port"));
            //向consul注册服务
            wsServiceId = RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).
                    doRegister(RegistryService.build(NetUtil.getHost() + "/" + ConfigUtil.get("service.name"),
                            NetUtil.getHost(),
                            ConfigUtil.getInteger("websocket.connect.port"),
                            ConfigUtil.get("service.version")));

            //推送消息服务器
            pushServerBootStrap = new ServerBootstrap();
            pushServerBootStrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new PushWebSocketServerInitializer());
            //绑定端口
            pushChannelFuture = pushServerBootStrap.bind(ConfigUtil.getInteger("websocket.connect.push.port"));

            wsPushServiceId = RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).
                    doRegister(RegistryService.build(NetUtil.getHost() + "/" + ConfigUtil.get("service.push.name"),
                            NetUtil.getHost(),
                            ConfigUtil.getInteger("websocket.connect.push.port"),
                            ConfigUtil.get("service.push.version")));
        } catch (Exception e) {
            logger.error("Acceptor Server Start ERROR", e);
            throw new RuntimeException(e);
        } finally {
            if (null != channelFuture) {
                channelFuture.sync().channel().closeFuture().sync();
            }
            if (null != pushChannelFuture) {
                pushChannelFuture.sync().channel().closeFuture().sync();
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
        if(wsServiceId != null) {
            logger.info("unregister websocket service!");
            RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).doUnRegister(wsServiceId);
        }
        if(wsPushServiceId != null) {
            logger.info("unregister websocket push service!");
            RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).doUnRegister(wsPushServiceId);
        }
     }


}
