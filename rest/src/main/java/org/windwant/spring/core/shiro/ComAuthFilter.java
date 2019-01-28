package org.windwant.spring.core.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ajax请求 session失效
 * Created by Administrator on 2017/8/18.
 */
public class ComAuthFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //AJAX请求
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        if (servletRequest.getHeader("x-requested-with") != null
                && servletRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                servletResponse.setHeader("sessionstatus", "timeout");
            }else if(getSubject(request, response).getSession().getAttribute("kickout") != null) {
                    servletResponse.setHeader("sessionstatus", "kickout");
                getSubject(request, response).getSession().stop();
            }
            return false;
        }
        if(!servletResponse.containsHeader("Access-Control-Allow-Origin")){
            servletResponse.addHeader("Access-Control-Allow-Origin", servletRequest.getHeader("Origin"));
            servletResponse.addHeader("Access-Control-Allow-Credentials", Boolean.TRUE.toString());
        }
        return super.onAccessDenied(request, response);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        Session curSession = subject.getSession();
        if(curSession.getAttribute("kickout") != null){
            return false;
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
