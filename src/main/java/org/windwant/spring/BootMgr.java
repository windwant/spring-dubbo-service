package org.windwant.spring;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by aayongche on 2016/8/19.
 */
public class BootMgr {

    private static AtomicInteger times = new AtomicInteger();

    public static Integer increment(){
        return times.incrementAndGet();
    }

    public static Integer decrement(){
        return times.decrementAndGet();
    }
}
