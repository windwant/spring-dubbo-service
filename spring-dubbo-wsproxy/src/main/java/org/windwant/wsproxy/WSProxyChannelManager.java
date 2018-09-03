package org.windwant.wsproxy;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 通道管理
 */
public class WSProxyChannelManager {

    public static final Map<String, Channel> userChannels;
    public static final Map<String, Channel> busChannels;

    private static final Logger logger = LoggerFactory.getLogger(WSProxyChannelManager.class);

    static {
        userChannels = new HashMap<>();
        busChannels = new HashMap<>();
    }

    public static Channel getUserChannel(String userId) {
        return userChannels.get(userId);
    }

    public static void registerUserChannel(String userId, Channel channel) {
        userChannels.put(userId, channel);
    }

    public static void removeUserChannel(String userId) {
        userChannels.remove(userId);
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

}
