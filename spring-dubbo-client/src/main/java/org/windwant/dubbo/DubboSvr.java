package org.windwant.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.windwant.common.api.DubboService;

/**
 * Created by Administrator on 2018/2/7.
 */
public class DubboSvr {
    private static Logger logger = LoggerFactory.getLogger(DubboSvr.class);
    public static DubboService dubboService;

    static {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/dubbo-consumer.xml");
            dubboService = (DubboService) context.getBean("dubbosvr");
            context.registerShutdownHook();
            logger.info("init dubbox service success... ");
        } catch (Exception e) {
            logger.error("init dubbox service error",e);
        }
    }
}
