package org.windwant.spring.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by windwant on 2016/11/9.
 */
public class TestAppListener implements ApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(DevAppListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        logger.info("Test ApplicationEvent: {}", applicationEvent.getClass().getName());
    }
}
