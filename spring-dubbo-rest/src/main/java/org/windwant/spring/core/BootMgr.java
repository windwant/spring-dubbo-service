package org.windwant.spring.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * BootMgr
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
