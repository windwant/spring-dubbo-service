package org.windwant.bus;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * channel 管理
 * Created by Administrator on 18-4-10.
 */
public class BusServerChannelMgr {
    public static ConcurrentHashMap<String, Channel> websocketChannel = new ConcurrentHashMap<>();
}
