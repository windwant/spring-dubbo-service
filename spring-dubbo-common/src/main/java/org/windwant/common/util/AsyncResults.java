package org.windwant.common.util;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.remoting.exchange.ResponseFuture;
import com.alibaba.dubbo.rpc.protocol.dubbo.DecodeableRpcResult;
import org.slf4j.Logger;

/**
 * Hello world!
 *
 */
public class AsyncResults
{

    public static void dealAsyncResult(ResponseFuture future, Logger logger){
        future.setCallback(new ResponseCallback() {
            @Override
            public void done(Object response) {
                try {
                    DecodeableRpcResult rpcResult = (DecodeableRpcResult) response;
                    logger.info("result: {}", rpcResult.getValue());
                } catch (Exception e) {
                }
            }

            @Override
            public void caught(Throwable throwable) {
            }
        });
    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
