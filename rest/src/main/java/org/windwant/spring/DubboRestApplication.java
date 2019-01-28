package org.windwant.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.windwant.dubbo.DubboSvr;

/**
 * Hello world!
 * @RestController = @Controller + @ResponseBody
 * @SpringBootApplication = @Configration +  @EnableAutoConfiguration + @ComponentScan
 * @ServletComponentScan scan servlet filter interceptor listener
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ServletComponentScan
@PropertySource({"classpath:config.properties"})
@EnableScheduling //定时任务
@ImportResource({"classpath:disconf/disconf.xml"}) //disconf 统一配置 }) //
public class DubboRestApplication
{
    public static void main( String[] args )
    {
        SpringApplication sa = new SpringApplication(DubboRestApplication.class);
//        sa.addListeners(new DevAppListener());//注册Listener
        ApplicationContext ctx = sa.run(args);
        //初始化dubbo服務
        DubboSvr.main(null);

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
