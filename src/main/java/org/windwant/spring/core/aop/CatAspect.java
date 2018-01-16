package org.windwant.spring.core.aop;

//import com.dianping.cat.Cat;
//import com.dianping.cat.message.Message;
//import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by windwant on 2016/11/23.
 */
//@Aspect
//@Component
public class CatAspect {

    private static final String CAT_TYPE = "Sboot.log";

    private static final String CAT_TYPE_NAME = "Sboot.log";

    private static final String CAT_METHOD_NAME = "hello";

    @Pointcut("execution(* org.windwant.spring.service.impl.BootServiceImpl.*(..))")
    public void log(){}

    @Around("log()")
    public Object preLog(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        String methodName = jp.getSignature().getName();
//        Transaction tran = Cat.newTransaction(CAT_TYPE, methodName);
        Object result = null;
//        try{
//            String nameValuePairs = getArgValue(args);
//            Cat.logEvent(CAT_TYPE_NAME, CAT_METHOD_NAME, Message.SUCCESS, nameValuePairs);
//            result = jp.proceed(args);
//            tran.setStatus(Transaction.SUCCESS);
//            System.out.println("Cat log: " + jp.getSignature().getDeclaringType() + " " +  methodName + "(" + nameValuePairs + ")");
//        }catch (Exception e){
//            tran.setStatus(e.getClass().getSimpleName());
//        }finally {
//            tran.complete();
//        }
        return result;
    }

    private String getArgValue(Object[] args){
        if(null == args || args.length == 0) return null;

        return Arrays.asList(args).stream().filter(obj-> obj != null).map(Object::toString).collect(Collectors.joining(","));
    }
}
