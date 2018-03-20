package org.windwant.spring.config;

import org.hibernate.validator.HibernateValidator;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.windwant.spring.core.interceptor.BootInterceptor;
import org.windwant.spring.core.mybatis.MapperScannerConfigurerProxy;

import javax.servlet.MultipartConfigElement;

/**
 * Created by Administrator on 2018/1/16.
 */
@Configuration
public class ApplicationConfig {

    @Configuration
    public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

        /**
         * 自定义拦截器
         * @param registry
         */
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new BootInterceptor()).addPathPatterns("/**");
            super.addInterceptors(registry);
        }

        /**
         * 跨域处理 映射所有路径 允许所有来源 以下方法请求
         * @param registry
         */
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH");
        }

        /**
         * 初始页面
         * @param registry
         */
        @Override
        public void addViewControllers(ViewControllerRegistry registry ) {
            registry.addViewController("/").setViewName( "forward:/index.html" );
            registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
            super.addViewControllers(registry);
        }
    }

    /**
     * mybatis mapper 扫描
     * @return
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurerProxy mapperScannerConfigurerProxy = new MapperScannerConfigurerProxy();
        mapperScannerConfigurerProxy.setBasePackage("org.windwant.spring.mapper");
        return mapperScannerConfigurerProxy;
    }

    /**
     * 验证信息 message
     * @return
     */
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(){
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
        ReloadableResourceBundleMessageSource rrbms = new ReloadableResourceBundleMessageSource();
        rrbms.setBasename("classpath:/lang/messages");
        rrbms.setUseCodeAsDefaultMessage(false);
        rrbms.setDefaultEncoding("UTF-8");
        localValidatorFactoryBean.setValidationMessageSource(rrbms);
        return localValidatorFactoryBean;
    }

    /**
     * springboot上传文件临时目录报错
     * 解决方法：
     * 1、重启服务，重新生成目录；
     * 2、手动创建该目录；
     * 3、在启动服务的main方法中，添加以下代码，指定上传文件的目录：
     * @Bean
     * MultipartConfigElement multipartConfigElement() {
     *     MultipartConfigFactory factory =new MultipartConfigFactory(); 
     *     factory.setLocation("/data/apps/temp"); 
     *     return factory.createMultipartConfig();
     * }
     * 4、在应用的配置文件中添加以下配置，原理同3：
     * server:
     *     tomcat:
     *         basedir: /data/apps/temp
     **/
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("/data/apps/temp");
        return factory.createMultipartConfig();

    }
}
