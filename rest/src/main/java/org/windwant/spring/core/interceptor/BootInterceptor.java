package org.windwant.spring.core.interceptor;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.windwant.spring.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BootInterceptor
 */
public class BootInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BootInterceptor.class);

    @Autowired
    private Meter requestMeter;
    @Autowired
    private Counter requestCount;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String url = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
        String param;
        if(httpServletRequest.getMethod().equals("POST")){
            //登录 只记录用户名
            if("/login".equals(url)){
                param = "user: " + StringUtil.reflectionToString(httpServletRequest.getParameterMap().get("name"));
            }else {
                param = StringUtil.reflectionToString(httpServletRequest.getParameterMap());
            }
        }else {
            param = httpServletRequest.getQueryString();
        }
        logger.info("begin request: {}, query params: {}", url, param);

//        requestMeter.mark();
//        requestCount.inc();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String url = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
        logger.info("end request: {}, query params: {}", url, httpServletRequest.getQueryString());
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        if (e != null) {
            String url = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
            logger.info("end request with exception: {}, query params: {}", url, httpServletRequest.getQueryString());
        }
    }
}
