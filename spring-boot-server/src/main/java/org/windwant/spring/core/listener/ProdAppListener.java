package org.windwant.spring.core.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by windwant on 2016/11/9.
 */
public class ProdAppListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        System.out.println("Prod ApplicationEvent: " + applicationEvent.getClass().getName());
    }
}
