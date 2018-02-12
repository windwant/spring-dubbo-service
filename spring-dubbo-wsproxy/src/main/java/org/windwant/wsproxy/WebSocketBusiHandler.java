package org.windwant.wsproxy;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.api.DubboService;
import org.windwant.common.api.model.Guest;
import org.windwant.protobuf.BootRequestResponse.BootRequest;
import org.windwant.wsproxy.util.WSUtil;

/**
 * 业务处理 handler
 */
public class WebSocketBusiHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketBusiHandler.class);
    

    public static void dealBusi(ChannelHandlerContext ctx, BootRequest bootRequest, DubboService dubboService) {
        try {
            //业务处理
            Guest guest = new Guest();
            guest.setName(bootRequest.getName());
            guest.setSex(bootRequest.getSex());
            guest.setAccessTime(bootRequest.getAccessTime());
            logger.info("receive request: {}", guest.toString());
            String response =  dubboService.getSysTime(guest);
            logger.info("response message: {}", response);
            WSUtil.response(ctx.channel(), bootRequest.getRequestCode(), bootRequest.getRequestCode(), response);
        } catch (Exception e) {
            logger.warn("dealBusi error {}!", e);
            WSUtil.responseThenClose(ctx.channel(), bootRequest.getRequestCode(), -1, "handle failed");
        }
    }

}
