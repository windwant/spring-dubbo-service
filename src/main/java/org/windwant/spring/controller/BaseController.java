package org.windwant.spring.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/16.
 */
public class BaseController {
    protected Map<String, Object> response(Integer code, String msg){
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("msg", msg);
        return response;
    }

    protected Map<String, Object> response(Integer code, String msg, Object result){
        Map<String, Object> response = response(code, msg);
        response.put("result", result);
        return response;
    }
}
