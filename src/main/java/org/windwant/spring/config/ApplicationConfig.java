package org.windwant.spring.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.windwant.spring.core.interceptor.BootInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */
@Configuration
public class ApplicationConfig {

    @Configuration
    public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

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

//        @Override
//        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//            FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//            FastJsonConfig fastJsonConfig = new FastJsonConfig();
//            fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//            //处理中文乱码问题
//            List<MediaType> fastMediaTypes = new ArrayList<>();
//            fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//            fastConverter.setSupportedMediaTypes(fastMediaTypes);
//            fastConverter.setFastJsonConfig(fastJsonConfig);
//            converters.add(fastConverter);
//        }
    }

    /**
     * 验证
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
}
