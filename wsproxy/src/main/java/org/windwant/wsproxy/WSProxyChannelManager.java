package org.windwant.wsproxy;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.windwant.wsproxy.util.ConsulUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 通道管理
 */
public class WSProxyChannelManager {
    //连接通道
    public static final Map<String, Channel> connectChannels;
    //推送消息通道
    public static final Map<String, Channel> busChannels;

    private static final Logger logger = LoggerFactory.getLogger(WSProxyChannelManager.class);

    static {
        connectChannels = new HashMap<>();
        busChannels = new HashMap<>();
    }

    public static Channel getConnectChannel(String key) {
        return connectChannels.get(key);
    }

    public static void registerConnectChannel(String key, Channel channel) {
        connectChannels.put(key, channel);
    }

    public static void removeConnectChannel(String key) {
        connectChannels.remove(key);
    }

    public static Channel getBusChannel(String ip) {
        return busChannels.get(ip);
    }

    public static void registerBusChannel(String ip, Channel channel) {
        busChannels.put(ip, channel);
    }

    public static void removeBusChannel(String ip) {
        busChannels.remove(ip);
    }

    public static String mapRequestCodeToChannel(String requestCode){
        switch (requestCode){
            case "1": return "channel-time";
            case "2": return "channel-login";
            default: return "channel-" + requestCode;
        }
    }

    public static String mapBusRequestCodeToChannel(String requestCode){
        switch (requestCode){
            case "1": return "bus_channel-time";
            case "2": return "bus_channel-login";
            default: return "bus_channel-" + requestCode;
        }
    }

    /**
     * 从本地缓存通道移除，并删除consul kv 直接关闭通道
     */
    public static void removeChannel(String requestCode, Channel channel) {
        if (!StringUtils.isEmpty(requestCode)) {
            removeConnectChannel(requestCode);
            ConsulUtil.removeRequestChannel(requestCode);
        }

        if (null != channel && channel.isActive()) {
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            channel.close();
        }
    }
}
