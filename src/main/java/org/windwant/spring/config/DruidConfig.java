package org.windwant.spring.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.BeanTypeAutoProxyCreator;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.windwant.spring.service.BootService;

import java.util.Arrays;

/**
 * Created by Administrator on 2018/2/6.
 */
@Configuration
public class DruidConfig {

    /**
     * 注册 StatViewServlet druid web页面使用
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        return reg;
    }

    @Bean
    public FilterRegistrationBean druidWebStatFilter(){
        FilterRegistrationBean reg = new FilterRegistrationBean();
        reg.setFilter(new WebStatFilter());
        reg.setUrlPatterns(Arrays.asList("/*"));
        reg.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        reg.addInitParameter("sessionStatMaxCount", "1000");
        reg.addInitParameter("sessionStatEnable", "true");
        reg.addInitParameter("principalSessionName", "druid.user");
        reg.addInitParameter("profileEnable", "true");
        return reg;
    }

    /**
     * Spring和Jdbc的关联监控。
     * DruidStatInterceptor:标准的Spring MethodInterceptor。可以灵活进行AOP配置
     * Advice
     * @return
     */
    @Bean
    public DruidStatInterceptor interceptorNames(){
        DruidStatInterceptor inc = new DruidStatInterceptor();
        return inc;
    }

    //=====================按类型拦截 配置Spring监控============================================
    /**
     * 按类型拦截配置
     * @return
     */
    @Bean
    public BeanTypeAutoProxyCreator beanTypeAutoProxyCreator(){
        BeanTypeAutoProxyCreator cut = new BeanTypeAutoProxyCreator();
        cut.setTargetBeanType(BootService.class);
        cut.setInterceptorNames("interceptorNames");
        return cut;
    }

    //=====================按方法名正则匹配拦截 配置Spring监控====================================

    /**
     * pointcut
     * @return
     */
    @Bean
    public JdkRegexpMethodPointcut jdkRegexpMethodPointcut(){
        JdkRegexpMethodPointcut cut = new JdkRegexpMethodPointcut();
        cut.setPatterns("org.windwant.spring.mapper.*");
        return cut;
    }

    /**
     * Advisor
     * @param pointcut
     * @param interceptor
     * @return
     */
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(JdkRegexpMethodPointcut pointcut, DruidStatInterceptor interceptor){
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);
        return advisor;
    }

    /**
     * AOP proxy based on beans in Spring
     * @return
     */
    @Bean
    public ProxyFactoryBean proxyFactoryBean(){
        ProxyFactoryBean proxy = new ProxyFactoryBean();
        proxy.setInterceptorNames("defaultPointcutAdvisor");
        return proxy;
    }
}
