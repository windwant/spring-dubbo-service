package org.windwant.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.windwant.common.api.DubboService;

/**
 * Hello world!
 *
 */
public class BootClient
{
    private static final Logger logger = LoggerFactory.getLogger(BootClient.class);
    public static void main( String[] args )
    {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("dubbox-consumer.xml");
        ctx.start();
        DubboService client = (DubboService) ctx.getBean("dubbosvr");
        while (true){
            client.getSysTime();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
