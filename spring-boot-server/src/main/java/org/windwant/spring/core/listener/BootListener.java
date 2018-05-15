package org.windwant.spring.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.spring.core.BootMgr;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * BootListener
 */
@WebListener
public class BootListener implements ServletContextListener{
    private static final Logger logger = LoggerFactory.getLogger(BootListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("listener init... : {}", BootMgr.increment());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("listener destroy... : {}", BootMgr.decrement());
    }
}
