package org.windwant.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.windwant.common.api.DubboTXServicex;
import org.windwant.common.api.DubboTXServicey;

/**
 * Created by Administrator on 2018/2/7.
 */
public class DubboTXSvr {
    private static Logger logger = LoggerFactory.getLogger(DubboTXSvr.class);
    public static DubboTXServicex dubboTXServicex;
    public static DubboTXServicey dubboTXServicey;

    static {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/dubbo-tx-consumer.xml");
            dubboTXServicex = (DubboTXServicex) context.getBean("serverx");
            dubboTXServicey = (DubboTXServicey) context.getBean("servery");
            context.registerShutdownHook();
            logger.info("init dubbox service success... ");
        } catch (Exception e) {
            logger.error("init dubbox service error",e);
        }
    }

    public static void main(String[] args) {
    }
}
