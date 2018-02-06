package org.windwant.spring.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.windwant.spring.Constants;
import org.windwant.spring.mapper.MySelMapper;
import org.windwant.spring.mapper.MySelRMapper;
import org.windwant.spring.model.Guest;
import org.windwant.spring.model.Response;
import org.windwant.spring.model.User;
import org.windwant.spring.service.BootService;
import org.windwant.spring.util.LangUtil;

import java.util.Map;

/**
 * BootServiceImpl
 */
@Service
public class BootServiceImpl implements BootService {
    private static final Logger logger = LoggerFactory.getLogger(BootServiceImpl.class);

    @Autowired
    LangUtil langUtil;

    @Autowired
    MySelMapper mySelMapper;
    @Autowired
    MySelRMapper mySelRMapper;

    @Override
    public String hello(String name){
        String message = "Hello " + name + ", welcome to my world!";
        logger.info(message);
        String source = mySelMapper.getResult(1);
        logger.info("mapper from @: {}", mySelMapper.getStringResult(1));
        logger.info("mapper from xml: {}", source);

        logger.info("data from {}", source);
        return message + " from " + source;
    }

    public String hellox(Guest guest){
        String message = "Hellox " + guest.getName() + ", welcome to my world!";
        logger.info(message);
        logger.info("guest: {}", guest.toString());
        String source = mySelRMapper.getResult(1);
        logger.info("mapper from @: {}", mySelRMapper.getStringResult(1));
        logger.info("mapper from xml: {}", source);
        logger.info("data from {}", source);
        return message + " from " + source;
    }

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> login(User user) {
        String msg = Constants.SUCCESS;

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPasswd(), false);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        //从session中获取之前请求的验证码 验证
        String code = (String) session.getAttribute(Constants.SESSION_KEY_IMAGE);
        if (code == null || !code.toLowerCase().equals(user.getCode().toLowerCase())) {
            logger.warn("login user {} code validate failed: {}", user.getUserName(), langUtil.getMsg("login.codeError"));
            return Response.response(-1, langUtil.getMsg("login.codeError"));
        }

        try {
            currentUser.login(token);
        }catch(UnknownAccountException uae){
            logger.warn("login user {} login failed: {}", user.getUserName(), langUtil.getMsg("login.userNotExisted"));
            msg= langUtil.getMsg("login.userNotExisted");
        }catch(IncorrectCredentialsException ice){
            logger.warn("login user {} login failed: {}", user.getUserName(), langUtil.getMsg("login.pwdErr"));
            msg= langUtil.getMsg("login.pwdErr");
        }catch(LockedAccountException lae){
            logger.warn("login user {} login failed: {}", user.getUserName(), langUtil.getMsg("login.accountLocked"));
            msg= langUtil.getMsg("login.accountLocked");
        }catch(DisabledAccountException dae){
            logger.warn("login user {} login failed: {}", user.getUserName(), langUtil.getMsg("login.accountDisabled"));
            msg= langUtil.getMsg("login.accountDisabled");
        }catch(AuthenticationException e){
            logger.warn("login user {} login failed: {}", user.getUserName(), langUtil.getMsg("login.userOrPwdErr"));
            msg= langUtil.getMsg("login.userOrPwdErr");
        }

        //验证是否登录成功
        if(currentUser.isAuthenticated()){
            User admin = User.build(user.getUserName(), user.getPasswd(), user.getStatus());
            SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_KEY_USER, user);
            SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_DRUID_USER, user.getUserName());
            logger.info("login user {} login success", user.getUserName());
            return Response.response(0, Constants.SUCCESS, user);
        }else{
            token.clear();
            return Response.response(-1, msg);//重新登录
        }
    }
}
