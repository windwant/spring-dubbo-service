package org.windwant.spring.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.spring.util.StringUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * BootFilter
 */
@WebFilter
public class BootFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(BootFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("filter init...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.info("request, path: {}, param: {}", request.getServletPath(), StringUtil.reflectionToString(request.getParameterMap()));
        if(request.getServletPath().startsWith("/test")){//test example
            request.getRequestDispatcher("/hello/test").forward(servletRequest, servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("filter destroy...");
    }
}
