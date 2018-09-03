package org.windwant.proxy;

import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.dubbo.DubboSvr;
import org.windwant.protocal.DubboServicePro;
import org.windwant.protocal.util.ProtobufUtil;

import java.util.Map;

/**
 * Created by Administrator on 2018/2/7.
 */
public class ProxyBusiHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProxyBusiHandler.class);
    public static void getBusiResponse( DubboServicePro.DubboRequest request, ChannelHandlerContext ctx) throws Exception {
        DubboServicePro.DubboResponse dubboResponse = DubboServicePro.DubboResponse.getDefaultInstance();
        try {
        //时间请求
        if(request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.TimeRequest){
            ByteString bytes = request.getUnknownFields().getField(DubboServicePro.TimeRequest.REQUEST_FIELD_NUMBER).getLengthDelimitedList().get(0);
            DubboServicePro.TimeRequest timeRequest =  DubboServicePro.TimeRequest.parseFrom(bytes);
            logger.info("receive request: {}", timeRequest.toString());
            Map<String, Object> response =  DubboSvr.dubboService.getSysTime(timeRequest.getAccessTime());
            logger.info("response message: {}", response);
            dubboResponse = ProtobufUtil.buildTimeResponse(response, request.getRequestCode().getNumber());
        }else if(request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.LoginRequest){
            ByteString bytes = request.getUnknownFields().getField(DubboServicePro.LoginRequest.REQUEST_FIELD_NUMBER).getLengthDelimitedList().get(0);
            DubboServicePro.LoginRequest loginRequest =  DubboServicePro.LoginRequest.parseFrom(bytes);
            logger.info("receive request: {}", loginRequest.toString());
            Map<String, Object> response =  DubboSvr.dubboService.login(loginRequest.getUserName(), loginRequest.getPasswd(), loginRequest.getCode());
            logger.info("response message: {}", response);
            dubboResponse = ProtobufUtil.buildLoginResponse(response, request.getRequestCode().getNumber());
        }else if (request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.BaseRequest){
            dubboResponse = ProtobufUtil.buildDubboResponse(0, "SUCCESS");
        }
        ctx.writeAndFlush(dubboResponse);
        } catch (Exception e) {
            logger.warn("dealBusi failed {}!", e);
            ctx.writeAndFlush(ProtobufUtil.buildDubboResponse(-1, e.getMessage()))
                    .addListener(channelFuture -> ctx.close());

        }
    }
}
