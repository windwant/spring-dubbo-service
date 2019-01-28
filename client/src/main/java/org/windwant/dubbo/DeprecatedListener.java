package org.windwant.dubbo;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.listener.DeprecatedInvokerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.ConfigUtil;

/**
 * DeprecatedListener
 */
public class DeprecatedListener extends DeprecatedInvokerListener {
    private static final Logger logger = LoggerFactory.getLogger(DeprecatedListener.class);

    @Override
    public void referred(Invoker<?> invoker) throws RpcException {
        super.referred(invoker);
        if(ConfigUtil.getList("deprecated.service.list").contains(invoker.getInterface().getSimpleName())) {
            logger.error("The service " + invoker.getInterface().getName() + " is DEPRECATED! Declare from " + invoker.getUrl());
        }
    }
}
