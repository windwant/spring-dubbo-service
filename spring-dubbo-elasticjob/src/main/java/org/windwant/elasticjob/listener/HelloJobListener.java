package org.windwant.elasticjob.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 18-4-12.
 */
public class HelloJobListener implements ElasticJobListener {
    private static final Logger logger = LoggerFactory.getLogger(HelloJobListener.class);
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        logger.info("job {}, param: {}, total sharding: {}, current sharding: {}",
                shardingContexts.getJobName(),
                shardingContexts.getShardingItemParameters().get(shardingContexts.getJobEventSamplingCount()),
                shardingContexts.getShardingTotalCount(),
                shardingContexts.getJobEventSamplingCount());
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        logger.info("job {} executed finished",
                shardingContexts.getJobName());
    }
}
