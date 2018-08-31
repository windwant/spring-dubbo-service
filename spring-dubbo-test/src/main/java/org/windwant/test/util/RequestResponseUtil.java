package org.windwant.test.util;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.windwant.common.util.DateUtil;
import org.windwant.protocal.DubboServicePro;

/**
 * Created by Administrator on 2018/2/9.
 */
public class RequestResponseUtil {
    public static DubboServicePro.DubboRequest getDubboRequest(int type) {
        DubboServicePro.DubboRequest dubboRequest = null;
        if (type == 1) {//时间请求测试
            DubboServicePro.TimeRequest.Builder builder = DubboServicePro.TimeRequest.newBuilder();
            builder.setAccessTime(DateUtil.getCurrentTime());
            dubboRequest = DubboServicePro.DubboRequest.newBuilder()
                    .setRequestCode(DubboServicePro.DubboRequest.RequestCode.TimeRequest)
                    .setExtension(DubboServicePro.TimeRequest.request, builder.build())
                    .build();
        } else {//登录请求测试
            DubboServicePro.LoginRequest.Builder builder = DubboServicePro.LoginRequest.newBuilder();
            builder.setUserName("roger")
                    .setPasswd("123456")
                    .setCode("1234")
                    .build();
            dubboRequest = DubboServicePro.DubboRequest.newBuilder()
                    .setRequestCode(DubboServicePro.DubboRequest.RequestCode.LoginRequest)
                    .setExtension(DubboServicePro.LoginRequest.request, builder.build())
                    .build();
        }
        return dubboRequest;
    }

    public static ByteBuf getByteBufDubboRequest(int type) {
        DubboServicePro.DubboRequest dubboRequest = getDubboRequest(type);
        if (type == 1) {//时间请求测试
            DubboServicePro.TimeRequest.Builder builder = DubboServicePro.TimeRequest.newBuilder();
            builder.setAccessTime(DateUtil.getCurrentTime());
            dubboRequest = DubboServicePro.DubboRequest.newBuilder()
                    .setRequestCode(DubboServicePro.DubboRequest.RequestCode.TimeRequest)
                    .setExtension(DubboServicePro.TimeRequest.request, builder.build())
                    .build();
        } else {//登录请求测试
            DubboServicePro.LoginRequest.Builder builder = DubboServicePro.LoginRequest.newBuilder();
            builder.setUserName("roger")
                    .setPasswd("123456")
                    .setCode("1234")
                    .build();
            dubboRequest = DubboServicePro.DubboRequest.newBuilder()
                    .setRequestCode(DubboServicePro.DubboRequest.RequestCode.LoginRequest)
                    .setExtension(DubboServicePro.LoginRequest.request, builder.build())
                    .build();
        }
        return Unpooled.wrappedBuffer(dubboRequest.toByteArray());
    }

    public static Object dealResponse(byte[] bytes){
        try {
            DubboServicePro.DubboResponse response = DubboServicePro.DubboResponse.parseFrom(bytes);
            if(response.getResponseCode().equals(DubboServicePro.DubboResponse.ResponseCode.TimeResponse)){
                ByteString subBytes = response.getUnknownFields().getField(DubboServicePro.TimeResponse.RESPONSE_FIELD_NUMBER).getLengthDelimitedList().get(0);
                DubboServicePro.TimeResponse timeResponse = DubboServicePro.TimeResponse.parseFrom(subBytes);
                return timeResponse;
            }else {
                ByteString subBytes = response.getUnknownFields().getField(DubboServicePro.LoginResponse.RESPONSE_FIELD_NUMBER).getLengthDelimitedList().get(0);
                DubboServicePro.LoginResponse loginResponse = DubboServicePro.LoginResponse.parseFrom(subBytes);
                return loginResponse;
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return "";
    }
}
