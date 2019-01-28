package org.windwant.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
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
@ServletComponentScan
@EnableAutoConfiguration
@PropertySource({"classpath:config.properties"})
@EnableScheduling //定时任务
@ImportResource({"META-INF/spring/dubbo-service.xml", "classpath:disconf/disconf.xml"}) //disconf 统一配置 }) //
public class BusiServiceApplication
{
    public static void main( String[] args )
    {
        SpringApplication sa = new SpringApplication(BusiServiceApplication.class);
        ApplicationContext ctx = sa.run(args);

        //consule init
//        ConsulMgr mgr = (ConsulMgr) ctx.getBean("consulMgr");
//        mgr.init();

//        启用console metric 输出
//        ConsoleReporter reporter = ctx.getBean(ConsoleReporter.class);
//        reporter.start(1, TimeUnit.SECONDS);

        //slf4jReporter metric 输出
//        Slf4jReporter reporterSJ = ctx.getBean(Slf4jReporter.class);
//        reporterSJ.start(1, TimeUnit.SECONDS);
    }
}
