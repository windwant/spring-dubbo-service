package org.windwant.wsproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.ConfigUtil;
import org.windwant.util.NetUtil;
import org.windwant.registry.RegistryFactory;
import org.windwant.registry.RegistryService;
import org.windwant.wsproxy.push.BusWSServerInitializer;
import org.windwant.wsproxy.util.ConsulUtil;

/**
 * WSProxy server
 *
 */
public class WSProxyServer {

    private static final Logger logger = LoggerFactory.getLogger(WSProxyServer.class);

    private EventLoopGroup connectGroup; //主线程组 处理连接
    private EventLoopGroup workGroup; //工作线程组 处理业务

    private String wsServiceId = null; //连接服务
    private String wsPushServiceId = null; //推送服务

    public static void main(String[] args) throws Exception {
        WSProxyServer wss = new WSProxyServer();
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
        connectGroup = new NioEventLoopGroup(2);

        workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap; //启动工具
        ChannelFuture channelFuture = null;
        ServerBootstrap busServerBootStrap;
        ChannelFuture busChannelFuture = null;
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(connectGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                            //如果要求高实时性，有数据就马上发送，该选项设置为true关闭Nagle算法
                            //如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_RCVBUF, 256 * 1024)//接收缓冲区
                    .option(ChannelOption.SO_SNDBUF, 256 * 1024)//发送缓冲区
                            //当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 8192)
                    .childHandler(new WSProxyInitializer());
            channelFuture = bootstrap.bind(ConfigUtil.getInteger("websocket.connect.port"));
            //向consul注册服务 host/serviceName
            wsServiceId = RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).
                    doRegister(RegistryService.build(NetUtil.getHost() + "/" + ConfigUtil.get("service.name"),
                            NetUtil.getHost(),
                            ConfigUtil.getInteger("websocket.connect.port"),
                            ConfigUtil.get("service.version")));

            //构造消息服务器
            busServerBootStrap = new ServerBootstrap();
            busServerBootStrap.group(connectGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new BusWSServerInitializer());
            //绑定端口
            busChannelFuture = busServerBootStrap.bind(ConfigUtil.getInteger("websocket.connect.bus.port"));

            //注册 bus server host/serviceName
            wsPushServiceId = RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).
                    doRegister(RegistryService.build(NetUtil.getHost() + "/" + ConfigUtil.get("service.bus.name"),
                            NetUtil.getHost(),
                            ConfigUtil.getInteger("websocket.connect.bus.port"),
                            ConfigUtil.get("service.bus.version")));
        } catch (Exception e) {
            logger.error("WSProxy Start failed", e);
            throw new RuntimeException(e);
        } finally {
            if (null != channelFuture) {
                channelFuture.sync().channel().closeFuture().sync();
            }
            if (null != busChannelFuture) {
                busChannelFuture.sync().channel().closeFuture().sync();
            }

            connectGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }
    }

    public void shutdownGraceFully() {
    	if (connectGroup != null) {
    		logger.info("connect group shutdown gracefully!");
    		connectGroup.shutdownGracefully();
    	}
    	if (workGroup != null) {
    		logger.info("work group shutdown gracefully!");
    		workGroup.shutdownGracefully();
    	}
        if(wsServiceId != null) {
            logger.info("deregister websocket service!");
            RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).doDeregister(wsServiceId);
        }
        if(wsPushServiceId != null) {
            logger.info("deregister websocket push service!");
            RegistryFactory.INSTANCE.
                    getRegistry(RegistryFactory.CONSUL).doDeregister(wsPushServiceId);
        }
     }


}
