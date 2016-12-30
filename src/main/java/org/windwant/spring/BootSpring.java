package org.windwant.spring;

import org.joda.time.LocalTime;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.windwant.spring.config.MybatisConfig;
import org.windwant.spring.mybatis.MapperScannerConfigurerProxy;
import org.windwant.spring.service.BootService;

/**
 * Hello world!
 * @RestController = @Controller + @ResponseBody
 * @SpringBootApplication = @Configration +  @EnableAutoConfiguration + @ComponentScan
 * @ServletComponentScan scan servlet filter interceptor listener
 */
@RestController
@SpringBootApplication
@ServletComponentScan
@Import({MybatisConfig.class})
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

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurerProxy configurer = new MapperScannerConfigurerProxy();
        configurer.setBasePackage("org.windwant.spring.mapper");
        return configurer;
    }

    public static void main( String[] args )
    {
        LocalTime localTime = new LocalTime();
        System.out.println( "Hello World!" + localTime.toString());
        localTime = localTime.plusHours(1);
        System.out.println("Hello agin World!" + localTime.toString());
        SpringApplication sa = new SpringApplication(BootSpring.class);
//        sa.addListeners(new DevAppListener());//注册Listener
        sa.run(args);

    }
}
