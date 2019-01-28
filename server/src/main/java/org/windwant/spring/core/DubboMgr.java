package org.windwant.spring.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * DubboMgr
 */
public class DubboMgr {

    private static AtomicInteger times = new AtomicInteger();

    public static AtomicInteger getLogin() {
        return login;
    }

    public static void setLogin(AtomicInteger login) {
        DubboMgr.login = login;
    }

    public static AtomicInteger getTimes() {
        return times;
    }

    public static void setTimes(AtomicInteger times) {
        DubboMgr.times = times;
    }

    private static AtomicInteger login = new AtomicInteger();

    public static Integer increment(){
        return times.incrementAndGet();
    }

    public static Integer decrement(){
        return times.decrementAndGet();
    }

    public static Integer login(){
        return login.incrementAndGet();
    }

    public static Integer logout(){
        return login.decrementAndGet();
    }
}
