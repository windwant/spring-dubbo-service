package org.windwant.wsproxy;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.api.DubboService;
import org.windwant.protocal.DubboServicePro;
import org.windwant.wsproxy.util.WSUtil;

import java.util.Map;

/**
 * 业务处理 handler
 */
public class WSBusiHandler {

    private static final Logger logger = LoggerFactory.getLogger(WSBusiHandler.class);


    public static void dealBusi(ChannelHandlerContext ctx, DubboServicePro.DubboRequest request, DubboService dubboService) {
        DubboServicePro.DubboResponse.Builder builder = DubboServicePro.DubboResponse.newBuilder();
        try {
            //时间请求处理
            if (request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.TimeRequest) {
                ByteString bytes = request.getUnknownFields().getField(DubboServicePro.TimeRequest.REQUEST_FIELD_NUMBER).getLengthDelimitedList().get(0);
                DubboServicePro.TimeRequest timeRequest = DubboServicePro.TimeRequest.parseFrom(bytes);
                logger.info("receive request: {}", timeRequest.toString());
                Map<String, Object> response = dubboService.getSysTime(timeRequest.getAccessTime());
                logger.info("response message: {}", response);
                DubboServicePro.TimeResponse timeResponse = DubboServicePro.TimeResponse.newBuilder()
                        .setResult((String) response.get("result"))
                        .setRequestCode(request.getRequestCode().getNumber())
                        .setResponseCode(request.getRequestCode().getNumber())
                        .build();
                builder = DubboServicePro.DubboResponse.newBuilder()
                        .setExtension(DubboServicePro.TimeResponse.response, timeResponse)
                        .setStatus((Integer) response.get("status"))
                        .setMsg((String) response.get("msg"))
                        .setResponseCode(DubboServicePro.DubboResponse.ResponseCode.TimeResponse);
            } else if (request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.LoginRequest) {//登录请求
                ByteString bytes = request.getUnknownFields().getField(DubboServicePro.LoginRequest.REQUEST_FIELD_NUMBER).getLengthDelimitedList().get(0);
                DubboServicePro.LoginRequest loginRequest = DubboServicePro.LoginRequest.parseFrom(bytes);
                logger.info("receive request: {}", loginRequest.toString());
                Map<String, Object> response = dubboService.login(loginRequest.getUserName(), loginRequest.getPasswd(), loginRequest.getCode());
                logger.info("response message: {}", response);
                DubboServicePro.LoginResponse loginResponse = DubboServicePro.LoginResponse.newBuilder()
                        .setResult(JSONObject.toJSONString(response))
                        .setRequestCode(request.getRequestCode().getNumber())
                        .setResponseCode(request.getRequestCode().getNumber())
                        .build();
                builder = DubboServicePro.DubboResponse.newBuilder()
                        .setExtension(DubboServicePro.LoginResponse.response, loginResponse)
                        .setStatus((Integer) response.get("status"))
                        .setMsg((String) response.get("msg"))
                        .setResponseCode(DubboServicePro.DubboResponse.ResponseCode.LoginResponse);
            }else if (request.getRequestCode() == DubboServicePro.DubboRequest.RequestCode.BaseRequest){//基本请求测试
                builder = DubboServicePro.DubboResponse.newBuilder()
                        .setStatus(0)
                        .setMsg("SUCCESS")
                        .setResponseCode(DubboServicePro.DubboResponse.ResponseCode.BaseResponse);
            }
            WSUtil.response(ctx.channel(), builder.build());
        } catch (Exception e) {
            logger.warn("dealBusi error {}!", e);
            WSUtil.responseThenClose(ctx.channel(), builder.setStatus(-1).setMsg(e.getMessage()).build());
        }
    }
}
