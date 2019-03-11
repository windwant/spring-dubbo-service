package org.windwant.serverx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 * @RestController = @Controller + @ResponseBody
 * @SpringBootApplication = @Configration +  @EnableAutoConfiguration + @ComponentScan
 * @ServletComponentScan scan servlet filter interceptor listener
 */
@EnableTransactionManagement//基于注解的事务管理 <tx:annotation-driven/>
@EnableAspectJAutoProxy(exposeProxy = true)//基于Aspect注解的事务管理 <aop:aspectj-autoproxy>； exposeProxy = true目标对象内部的自我调用的事务增强支持
@SpringBootApplication
@EnableAutoConfiguration
@PropertySource({"classpath:config.properties"})
@ImportResource({"spring/dubbo-service.xml", "spring/dubbo-tx-servicex.xml", "spring/dubbo-tx-servicey.xml"}) //
public class ServicexApplication
{
    public static void main( String[] args )
    {
        SpringApplication sa = new SpringApplication(ServicexApplication.class);
        ApplicationContext ctx = sa.run(args);
    }
}
