package org.windwant.spring.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/16.
 */
public class Response {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERROR = -1;
    public static final int CODE_500 = 500;
    public static final int CODE_403 = 403;

    public static final String MSG_SUCCESS = "OK";
    public static final String MSG_ERROR_EXCEPTION = "业务处理发生异常";
    public static final String MSG_ERROR_FORBIDDEN = "当前资源未被授权访问";


    public static Map<String, Object> response(Integer code, String msg){
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("msg", msg);
        return response;
    }

    public static Map<String, Object> response(Integer code, String msg, Object result){
        Map<String, Object> response = response(code, msg);
        response.put("result", result);
        return response;
    }
}
