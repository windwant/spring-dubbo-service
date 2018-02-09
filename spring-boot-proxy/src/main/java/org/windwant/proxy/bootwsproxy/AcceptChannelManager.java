package org.windwant.proxy.bootwsproxy;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 对用户通道和内部通道的管理
 *
 */
public class AcceptChannelManager {

    public static final Map<String, Channel> userChannels;

    private static final Logger logger = LoggerFactory.getLogger(AcceptChannelManager.class);

    static {
        userChannels = new HashMap<>();
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

}
