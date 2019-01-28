package org.windwant.wsproxy;

import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.dubbo.DubboSvr;
import org.windwant.protocal.DubboServicePro;
import org.windwant.protocal.util.ProtobufUtil;
import org.windwant.wsproxy.util.WSUtil;

import java.util.Map;

/**
 * 业务处理 handler
 */
public class WSBusiHandler {

    private static final Logger logger = LoggerFactory.getLogger(WSBusiHandler.class);


    public static void dealBusi(ChannelHandlerContext ctx, DubboServicePro.DubboRequest request) {
        DubboServicePro.DubboResponse dubboResponse = DubboServicePro.DubboResponse.getDefaultInstance();
        try {
            //时间请求处理
            if (request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.TimeRequest) {
                ByteString bytes = request.getUnknownFields().getField(DubboServicePro.TimeRequest.REQUEST_FIELD_NUMBER).getLengthDelimitedList().get(0);
                DubboServicePro.TimeRequest timeRequest = DubboServicePro.TimeRequest.parseFrom(bytes);
                logger.info("receive request: {}", timeRequest.toString());
                Map<String, Object> response = DubboSvr.dubboService.getSysTime(timeRequest.getAccessTime());
                logger.info("response message: {}", response);
                dubboResponse = ProtobufUtil.buildTimeResponse(response, request.getRequestCode().getNumber());
            } else if (request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.LoginRequest) {//登录请求
                ByteString bytes = request.getUnknownFields().getField(DubboServicePro.LoginRequest.REQUEST_FIELD_NUMBER).getLengthDelimitedList().get(0);
                DubboServicePro.LoginRequest loginRequest = DubboServicePro.LoginRequest.parseFrom(bytes);
                logger.info("receive request: {}", loginRequest.toString());
                Map<String, Object> response = DubboSvr.dubboService.login(loginRequest.getUserName(), loginRequest.getPasswd(), loginRequest.getCode());
                logger.info("response message: {}", response);
                dubboResponse = ProtobufUtil.buildLoginResponse(response, request.getRequestCode().getNumber());
            }else if (request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.BaseRequest){//基本请求测试
                dubboResponse = ProtobufUtil.buildDubboResponse(0, "SUCCESS");
            }
            WSUtil.response(ctx.channel(), dubboResponse);
        } catch (Exception e) {
            logger.warn("dealBusi failed {}!", e);
            WSUtil.responseThenClose(ctx.channel(), ProtobufUtil.buildDubboResponse(-1, e.getMessage()));
        }
    }
}
