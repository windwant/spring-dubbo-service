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
    public static final Map<String, Channel> pushChannels;

    private static final Logger logger = LoggerFactory.getLogger(WSProxyChannelManager.class);

    static {
        userChannels = new HashMap<>();
        pushChannels = new HashMap<>();
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

    public static Channel getPushChannel(String ip) {
        return pushChannels.get(ip);
    }

    public static void registerPushChannel(String ip, Channel channel) {
        pushChannels.put(ip, channel);
    }

    public static void removePushChannel(String ip) {
        pushChannels.remove(ip);
    }

}
