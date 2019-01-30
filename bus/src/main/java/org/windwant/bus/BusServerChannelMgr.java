package org.windwant.bus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.StringUtil;

import java.util.concurrent.ConcurrentHashMap;

/**
 * channel 管理
 * Created by Administrator on 18-4-10.
 */
public class BusServerChannelMgr {
    private static Logger logger = LoggerFactory.getLogger(BusServerChannelMgr.class);

    public static ConcurrentHashMap<String, Channel> getChannels() {
        return channels;
    }

    public static void setChannels(ConcurrentHashMap<String, Channel> channels) {
        BusServerChannelMgr.channels = channels;
    }

    private static ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();

    /**
     * 获取通道
     * @param channelName
     * @return
     */
    public static Channel getChannel(String channelName){
        if(StringUtil.isEmpty(channelName)) return null;
        Channel channel = channels.get(channelName);
        if(!channel.isActive()){
            logger.warn("channel in the mgr is inactive, will removed");
            return null;
        }
        return channel;
    }

    /**
     * 添加通道
     * @param channelName
     * @param channel
     * @return
     */
    public static Channel putChannel(String channelName, Channel channel){
        logger.info("channel put into mgr, key {}", channelName);
        return channels.put(channelName, channel);
    }

    public static void removeChannel(String channelName){
        if(StringUtil.isEmpty(channelName)) return;
        channels.remove(channelName);
    }

    public static int getChannelSize(){
        return channels.size();
    }

    public static void writeAndFlush(ByteBuf byteBuf){
        channels.values().stream().forEach(channel -> {
            channel.writeAndFlush(byteBuf);
        });
    }
}
