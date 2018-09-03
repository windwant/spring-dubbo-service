package org.windwant.spring.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.windwant.common.api.DubboService;
import org.windwant.common.api.model.User;
import org.windwant.spring.Constants;
import org.windwant.spring.mapper.UserMapper;
import org.windwant.spring.model.Response;
import org.windwant.spring.util.LangUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 通过dubbo rpc访问
 * Created by Administrator on 2018/2/6.
 */
@Service("dubbosvr")
public class DubboServiceImpl implements DubboService {
    private static final Logger logger = LoggerFactory.getLogger(DubboServiceImpl.class);

    @Override
    public Map<String, Object> getSysTime(String accessTime) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        logger.info("request time: {}, current time: {}", accessTime, currentTime);
        return Response.response(0, Constants.SUCCESS, "service tell  current time: " + currentTime);
    }

    @Autowired
    LangUtil langUtil;

    @Autowired
    UserMapper userMapper;

    /**
     * 登录
     * @param userName
     * @param passwd
     * @param code
     * @return
     */
    @Override
    public Map<String, Object> login(String userName, String passwd, String code) {
        String msg = Constants.SUCCESS;

        User user = userMapper.getUserByUserName(userName);
        if(user == null){
            logger.warn("login user {} login failed: {}", userName, langUtil.getMsg("login.userNotExisted"));
            return Response.response(-1, langUtil.getMsg("login.userNotExisted"));
        }

        if(!passwd.equals(user.getPasswd())){
            logger.warn("login user {} login failed: {}", userName, langUtil.getMsg("login.pwdErr"));
            return Response.response(-1, langUtil.getMsg("login.pwdErr"));
        }

        if(!code.equals(user.getCode())){
            logger.warn("login user {} code validate failed: {}", userName, langUtil.getMsg("login.codeError"));
            return Response.response(-1, langUtil.getMsg("login.codeError"));
        }

        User admin = User.build(userName, passwd, 1);

        logger.info("login user {} login success", user.toString());
        return Response.response(0, Constants.SUCCESS, admin.toString());

    }
}
