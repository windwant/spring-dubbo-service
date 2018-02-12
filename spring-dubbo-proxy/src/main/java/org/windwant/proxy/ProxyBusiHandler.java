package org.windwant.proxy;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.api.DubboService;
import org.windwant.common.api.model.Guest;
import org.windwant.protobuf.BootRequestResponse.BootRequest;
import org.windwant.protobuf.BootRequestResponse.BootResponse;

/**
 * Created by Administrator on 2018/2/7.
 */
public class ProxyBusiHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProxyBusiHandler.class);
    public static void getBusiResponse(DubboService dubboService, BootRequest bootrequest, ChannelHandlerContext ctx) throws Exception {
//        ByteString bytes = bootrequest.getUnknownFields().getField(BootRequest.REQUEST_FIELD_NUMBER).getLengthDelimitedList().get(0);
//        BootRequest request = BootRequest.parseFrom(bytes);
        Guest guest = new Guest();
        guest.setName(bootrequest.getName());
        guest.setSex(bootrequest.getSex());
        guest.setAccessTime(bootrequest.getAccessTime());
        logger.info("receive request: {}", guest.toString());
        String response =  dubboService.getSysTime(guest);
        logger.info("response message: {}", response);
        BootResponse.Builder builder = BootResponse.newBuilder();
        builder.setResult(response).setRequestCode(bootrequest.getRequestCode()).setRespCode(bootrequest.getRequestCode());
        BootResponse res = builder.build();
        ctx.writeAndFlush(res);

//        AsyncUtil.senResponse(channel, type);
    }
}
