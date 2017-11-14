package org.windwant.spring;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Slf4jReporter;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.windwant.spring.service.BootService;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 * @RestController = @Controller + @ResponseBody
 * @SpringBootApplication = @Configration +  @EnableAutoConfiguration + @ComponentScan
 * @ServletComponentScan scan servlet filter interceptor listener
 */
@RestController
@SpringBootApplication
@ServletComponentScan
@PropertySource({"classpath:config.properties"})
//@EnableScheduling //定时任务
public class BootSpring
{

    @RequestMapping("/{name}")
    String home(@PathVariable String name){
        return bootService.hello(name);
    }

    @Autowired
    private BootService bootService;

    public static void main( String[] args )
    {
        LocalTime localTime = new LocalTime();
        System.out.println( "Hello World!" + localTime.toString());
        localTime = localTime.plusHours(1);
        System.out.println("Hello agin World!" + localTime.toString());
        SpringApplication sa = new SpringApplication(BootSpring.class);
//        sa.addListeners(new DevAppListener());//注册Listener
        ApplicationContext ctx = sa.run(args);

        //启用console metric 输出
        ConsoleReporter reporter = ctx.getBean(ConsoleReporter.class);
        reporter.start(1, TimeUnit.SECONDS);

        //slf4jReporter metric 输出
        Slf4jReporter reporterSJ = ctx.getBean(Slf4jReporter.class);
        reporterSJ.start(1, TimeUnit.SECONDS);
    }
}
