package org.windwant.common.api;

import java.util.Map;

/**
 * Created by Administrator on 2018/2/6.
 */
public interface DubboService {
    Map<String, Object> getSysTime(String accessTime);

    Map<String, Object> login(String userName, String password, String code);
}
