package org.windwant.spring.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by aayongche on 2016/8/19.
 */
@Configuration
public class BootWebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BootInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
