package org.windwant.spring.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.windwant.common.util.HttpUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * BootJob 测试druid 数据源监控 定时任务
 * http://localhost:8081/druid/index.html
 */
//@Component
public class BootJob {
    private static final Logger logger = LoggerFactory.getLogger(BootJob.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final String dsUrlOne = "http://localhost:8081/hellox?name=kitty";
    private static final String dsUrlTwo = "http://localhost:8081/hello/";

    @Scheduled(fixedRate = 10000)
    public void helloJob(){
        logger.info("current time is: {}", dateFormat.format(new Date()));
        String url = dsUrlTwo + ThreadLocalRandom.current().nextInt(5);
        logger.info("test hello request url {}", url);
        HttpUtils.urlParamGet(url, 2);
    }

    @Scheduled(fixedRate = 20000)
    public void helloxJob(){
        logger.info("current time is: {}", dateFormat.format(new Date()));
        String url = dsUrlOne + ThreadLocalRandom.current().nextInt(5);
        logger.info("test hellox request url {}", url);
        HttpUtils.urlParamGet(url, 2);
    }
}
