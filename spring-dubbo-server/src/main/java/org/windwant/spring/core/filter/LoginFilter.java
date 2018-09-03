package org.windwant.spring.core.filter;

import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.spring.core.DubboMgr;
import org.windwant.spring.model.Response;

/**
 * dubbo login filter
 * refuse guest login
 */
public class LoginFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if(invocation.getMethodName().equals("login")){
            logger.info("login filter {} {}...", invocation.getMethodName(), invocation.getArguments());
            DubboMgr.login();
            logger.info("current login execute times: {}", DubboMgr.getLogin());
            if(invocation.getArguments() != null
                    && invocation.getArguments().length > 1
                    && invocation.getArguments()[0].toString().equals("guest")){
                logger.warn("login user guest login refused");
                return new RpcResult(Response.response(-1, "login user guest login refused"));
            }
        }
        return invoker.invoke(invocation);
    }
}
