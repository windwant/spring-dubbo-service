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
 *
 */
public class BusServer
{

    private static final Logger logger = LoggerFactory.getLogger(BusServer.class);
    Bootstrap bootstrap = new Bootstrap();
    public static final io.netty.channel.EventLoopGroup EventLoopGroup = new NioEventLoopGroup();
    public void init(){
        HealthClient healthClient = Consul.builder().withHostAndPort(
                HostAndPort.fromParts(ConfigUtil.get("consul.host"),
                        ConfigUtil.getInteger("consul.port")))
                .build()
                .healthClient();
        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances(NetUtil.getHost() + "/" + ConfigUtil.get("websocket.push.server")).getResponse();
        if(nodes != null && nodes.size() > 0) {
            ServiceHealth serviceHealth = nodes.get(0);
            bootstrap.group(EventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new BusServerInitializer());
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
        }else {
            logger.warn("websocket push service not available");
        }
    }
    public static void main( String[] args )
    {
        new BusServer().init();
    }
}
