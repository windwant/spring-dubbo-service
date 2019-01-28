package org.windwant.protocal.util;

import org.windwant.protocal.DubboServicePro;

import java.util.Map;

/**
 * Created by Administrator on 18-9-3.
 */
public class ProtobufUtil {

    public static DubboServicePro.DubboResponse.Builder getDubboResponseBuilder(int status, String msg, DubboServicePro.DubboResponse.ResponseCode responseCode){
        return DubboServicePro.DubboResponse.newBuilder()
                .setStatus(status)
                .setMsg(msg)
                .setResponseCode(responseCode);
    }

    public static DubboServicePro.DubboResponse buildDubboResponse(int status, String msg){
        return getDubboResponseBuilder(status, msg, DubboServicePro.DubboResponse.ResponseCode.BaseResponse).build();
    }

    public static DubboServicePro.DubboResponse buildLoginResponse(Map<String, Object> busiResult, int requestCode){
        DubboServicePro.LoginResponse loginResponse = DubboServicePro.LoginResponse.newBuilder()
                .setResult((String) busiResult.get("result"))
                .setRequestCode(requestCode)
                .setResponseCode(requestCode)
                .build();
        return getDubboResponseBuilder((Integer) busiResult.get("status"),
                (String) busiResult.get("msg"),
                DubboServicePro.DubboResponse.ResponseCode.LoginResponse)
                .setExtension(DubboServicePro.LoginResponse.response, loginResponse)
                .build();
    }

    public static DubboServicePro.DubboResponse buildTimeResponse(Map<String, Object> busiResult, int requestCode){
        DubboServicePro.LoginResponse loginResponse = DubboServicePro.LoginResponse.newBuilder()
                .setResult((String) busiResult.get("result"))
                .setRequestCode(requestCode)
                .setResponseCode(requestCode)
                .build();
        return getDubboResponseBuilder((Integer) busiResult.get("status"), (String) busiResult.get("msg"),
                DubboServicePro.DubboResponse.ResponseCode.LoginResponse)
                .setExtension(DubboServicePro.LoginResponse.response, loginResponse)
                .build();
    }
}
