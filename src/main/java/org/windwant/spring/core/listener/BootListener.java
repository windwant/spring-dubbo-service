package org.windwant.spring.core.listener;

import org.windwant.spring.core.BootMgr;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * BootListener
 */
@WebListener
public class BootListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("listener init... : " + BootMgr.increment());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("listener destroy... : " + BootMgr.decrement());
    }
}
