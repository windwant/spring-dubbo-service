package org.windwant.push;

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

import java.util.List;

/**
 * Hello world!
 *
 */
public class PushServer
{

    private static final Logger logger = LoggerFactory.getLogger(PushServer.class);
    Bootstrap bootstrap = new Bootstrap();
    public static final io.netty.channel.EventLoopGroup EventLoopGroup = new NioEventLoopGroup();
    public void init(){
        HealthClient healthClient = Consul.builder().withHostAndPort(
                HostAndPort.fromParts(ConfigUtil.get("consul.host"),
                        ConfigUtil.getInteger("consul.port")))
                .build()
                .healthClient();
        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances(ConfigUtil.get("websocket.push.server")).getResponse();
        if(nodes != null && nodes.size() > 0) {
            ServiceHealth serviceHealth = nodes.get(0);
            bootstrap.group(EventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new PushServerInitializer());
            ChannelFuture future = bootstrap.connect(serviceHealth.getService().getAddress(), serviceHealth.getService().getPort());
            try {
                new Thread(() -> {
                    new MqMonitor().start();
                }).start();

                logger.info("push server start");
                future.sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                EventLoopGroup.shutdownGracefully();
            }
        }
    }
    public static void main( String[] args )
    {
        new PushServer().init();
    }
}
