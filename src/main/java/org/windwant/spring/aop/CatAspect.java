package org.windwant.spring.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by windwant on 2016/11/23.
 */
@Aspect
public class CatAspect {

    private static final String CAT_TYPE = "Sboot.log";

    private static final String CAT_TYPE_NAME = "Sboot.log";

    private static final String CAT_METHOD_NAME = "hello";

    @Around("execution(* org.windwant.spring.service.impl.*(..))")
    public Object preLog(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        Transaction tran = Cat.newTransaction(CAT_TYPE, "hello");
        Object result = null;
        try{
            Cat.logEvent(CAT_TYPE_NAME, CAT_METHOD_NAME, Message.SUCCESS, getArgValue(args));
            result = jp.proceed(args);
            tran.setStatus(Transaction.SUCCESS);
        }catch (Exception e){
            tran.setStatus(e.getClass().getSimpleName());
        }finally {
            tran.complete();
        }
        return result;
    }

    private String getArgValue(Object[] args){
        if(null == args || args.length == 0) return null;

        return Arrays.asList(args).stream().filter(obj-> obj != null).map(Object::toString).collect(Collectors.joining(","));
    }
}
