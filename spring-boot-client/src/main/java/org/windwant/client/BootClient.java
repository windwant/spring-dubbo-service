package org.windwant.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.windwant.common.api.DubboService;
import org.windwant.common.api.model.Guest;

/**
 * Hello world!
 *
 */
@Deprecated
public class BootClient
{
    private static final Logger logger = LoggerFactory.getLogger(BootClient.class);
    public static void main( String[] args )
    {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("dubbox-consumer.xml");
        ctx.start();
        DubboService client = (DubboService) ctx.getBean("dubbosvr");
        Guest guest = new Guest();
        guest.setName("test");
        guest.setSex(1);
        while (true){
            client.getSysTime(guest);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
