package org.windwant.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.windwant.common.api.DubboRestService;
import org.windwant.common.api.DubboService;

/**
 * Created by Administrator on 2018/2/7.
 */
public class DubboSvr {
    private static Logger logger = LoggerFactory.getLogger(DubboSvr.class);
    public static DubboService dubboService;
    public static DubboRestService dubboRestService;

    static {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/dubbo-consumer.xml");
            dubboService = (DubboService) context.getBean("dubbosvr");
            dubboRestService = (DubboRestService) context.getBean("dubborestsvr");
            context.registerShutdownHook();
            test();
            logger.info("init dubbox service success... ");
        } catch (Exception e) {
            logger.error("init dubbox service error",e);
        }
    }

    public static void test(){
        System.out.println(dubboService.login("roger", "123456", "1234"));
        System.out.println(dubboService.login("guest", "123456", "1234"));
        System.out.println(dubboRestService.hello("roger"));
        System.out.println(dubboRestService.getStu(1));
    }

    public static void main(String[] args) {

    }
}
