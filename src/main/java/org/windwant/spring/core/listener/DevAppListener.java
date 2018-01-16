package org.windwant.spring.core.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
/**
 * Created by windwant on 2016/11/9.
 */
public class DevAppListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        System.out.println("Dev ApplicationEvent: " + applicationEvent.getClass().getName());
    }
}
