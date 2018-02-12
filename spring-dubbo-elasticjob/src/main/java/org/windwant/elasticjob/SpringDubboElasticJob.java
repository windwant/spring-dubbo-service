package org.windwant.elasticjob;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class SpringDubboElasticJob
{
    public static void main( String[] args )
    {
        new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }
}
