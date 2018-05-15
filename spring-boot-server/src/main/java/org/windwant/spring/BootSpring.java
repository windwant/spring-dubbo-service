package org.windwant.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
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
@EnableTransactionManagement
@SpringBootApplication
@ServletComponentScan
@EnableAutoConfiguration
@PropertySource({"classpath:config.properties"})
@PropertySource({"classpath:application.yml"})//取本地配置文件
@EnableScheduling //定时任务
@ImportResource({"META-INF/spring/dubbo-service.xml", "classpath:disconf/disconf.xml"}) //disconf 统一配置 }) //
public class BootSpring
{
    public static void main( String[] args )
    {
        SpringApplication sa = new SpringApplication(BootSpring.class);
//        sa.addListeners(new DevAppListener());//注册Listener
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
