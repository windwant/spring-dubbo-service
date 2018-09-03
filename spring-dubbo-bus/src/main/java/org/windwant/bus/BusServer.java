package org.windwant.bus;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.ServiceHealth;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.util.ConfigUtil;
import org.windwant.common.util.NetUtil;

import java.util.List;

/**
 * Hello world!
 * 消息服务 获取consul注册的netty服务，构造netty client 进行连接，保存channel
 * 启动消息队列监听，消息到达则通过上一步的channel发送出去
 */
public class BusServer
{

    private static final Logger logger = LoggerFactory.getLogger(BusServer.class);
    Bootstrap bootstrap = new Bootstrap();
    public static final io.netty.channel.EventLoopGroup EventLoopGroup = new NioEventLoopGroup();
    public void init(){
        //构造consul client
        HealthClient healthClient = Consul.builder().withHostAndPort(
                HostAndPort.fromParts(ConfigUtil.get("consul.host"),
                        ConfigUtil.getInteger("consul.port")))
                .build()
                .healthClient();
        //获取consul上注册的健康的服务 instance: host/
        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances(NetUtil.getHost() + "/" + ConfigUtil.get("bus.websocket.server")).getResponse();
        if(nodes != null && nodes.size() > 0) {
            ServiceHealth serviceHealth = nodes.get(0);
            //构造netty client
            bootstrap.group(EventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new BusServerInitializer());
            ChannelFuture future = bootstrap.connect(serviceHealth.getService().getAddress(), serviceHealth.getService().getPort());
            //启动消息队列监听
            try {
                new Thread(() -> {
                    new MqListener().start();
                }).start();

                logger.info("message bus server start...");
                future.sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                EventLoopGroup.shutdownGracefully();
            }
        }else {
            logger.warn("message bus service unavailable");
        }
    }
    public static void main( String[] args )
    {
        new BusServer().init();
    }
}
