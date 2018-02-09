
package org.windwant.dubbo.cat;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.*;
import com.dianping.cat.Cat;
import com.dianping.cat.Cat.Context;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * dubbo cat监控
 *
 * @author Jacarri
 */


@SuppressWarnings("all")
public class CatDubboFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CatDubboFilter.class);

/**
     * 记录业务日志
     */

    public void logFirst(Invocation invocation) {
        StringBuilder sb = new StringBuilder();
        sb.append("enter the " + invocation.getMethodName() + " method, args:[");
        if (invocation.getArguments() != null && invocation.getArguments().length > 0) {
            for (int i = 0; i < invocation.getArguments().length; i++) {
            	
                String argValue = "";
                if (invocation.getArguments()[i] != null) {
                    if (invocation.getArguments()[i] instanceof String) {
                        argValue = (String) invocation.getArguments()[i];
                    }
                    if (invocation.getArguments()[i] instanceof Integer) {
                        int argValueInt = (Integer) invocation.getArguments()[i];
                        argValue = String.valueOf(argValueInt);
                    }
                    if (invocation.getArguments()[i] instanceof Double) {
                        double argValueInt = (Double) invocation.getArguments()[i];
                        argValue = String.valueOf(argValueInt);
                    }
                    if (i < invocation.getArguments().length - 1) {
                        sb.append(argValue + ",");
                    } else {
                        sb.append(argValue);
                    }
                }
            }
        }
        sb.append("].");
        logger.info(sb.toString());
    }


    /**
     * 记录业务日志
     */

    public void logLast(Result result, Invocation invocation) {
        StringBuilder sb = new StringBuilder();
        sb.append("left the " + invocation.getMethodName() + " method, result:[ ");
        sb.append(result.getValue() + " ]");
        logger.info(sb.toString());
    }

    @Override
    @SuppressWarnings("all")
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean isProvider = RpcContext.getContext().isProviderSide();
        URL url = invoker.getUrl();
        Result result = null;
        String loggerName = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
        Transaction t = Cat.newTransaction("Service", loggerName);
        Cat.logEvent("dubbo.parameters", "application: " + url.getParameter(Constants.APPLICATION_KEY) + ", version: " + url.getParameter(Constants.DUBBO_VERSION_KEY));

        if(loggerName.contains("BootService.login")){
            Cat.logMetricForCount("LoginCount");
        }

        CatContext catContext = new CatContext();
        //dubbo provider
        if (isProvider) {
            Cat.logEvent("dubbo.provider", "ip: " + url.getHost() + " , port: " + String.valueOf(url.getPort()));
            catContext.addProperty(Context.ROOT, invocation.getAttachment(Context.ROOT));
            catContext.addProperty(Context.PARENT, invocation.getAttachment(Context.PARENT));
            catContext.addProperty(Context.CHILD, invocation.getAttachment(Context.CHILD));
            Cat.logRemoteCallServer(catContext);
        }
        //dubbo consumer
        else {
            Cat.logEvent("dubbo.consumer", "ip: " + url.getHost() + " , port: " + String.valueOf(url.getPort()));
            Cat.logRemoteCallClient(catContext);
            setAttachment(catContext);
        }

        try {
            logFirst(invocation);
            result = invoker.invoke(invocation);
            if (result.hasException()) {
                // 给调用接口出现异常进行打点
                Throwable throwable = result.getException();
                logger.error("business exception occured1 ！ERROR ", throwable);
                Event event;
                if (RpcException.class == throwable.getClass()) {
                    Throwable caseBy = throwable.getCause();
                    if (caseBy != null && TimeoutException.class.isAssignableFrom(caseBy.getClass())) {
                        event = Cat.newEvent("DUBBO_TIMEOUT_ERROR", loggerName);
                    } else {
                        event = Cat.newEvent("DUBBO_REMOTING_ERROR", loggerName);
                    }
                } else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
                    event = Cat.newEvent("DUBBO_REMOTING_ERROR", loggerName);
                } else {
                    event = Cat.newEvent("DUBBO_BIZ_ERROR", loggerName);
                }

                event.setStatus(result.getException());
                event.complete();
                t.addChild(event);
                t.setStatus(result.getException().getClass().getSimpleName());
            } else {
                t.setStatus(Transaction.SUCCESS);
                logLast(result, invocation);
            }
        } catch (Exception e) {
            logger.error("business exception occured2 ！ERROR ", e);
            t.setStatus("ERROR");
            Cat.logError(e);
            throw e;
        } finally {
            t.complete();
        }

        return result;
    }

    private void setAttachment(Context context) {
        RpcContext.getContext().setAttachment(Context.PARENT, context.getProperty(Context.PARENT));
        RpcContext.getContext().setAttachment(Context.CHILD, context.getProperty(Context.CHILD));
        RpcContext.getContext().setAttachment(Context.ROOT, context.getProperty(Context.ROOT));
    }
}

class CatContext implements Context {

    private Map<String, String> properties = new HashMap<>();

    @Override
    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }
}

