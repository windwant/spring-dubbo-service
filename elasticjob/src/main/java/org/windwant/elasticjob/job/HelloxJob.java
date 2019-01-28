package org.windwant.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.util.HttpUtils;

/**
 * Created by Administrator on 2018/2/12.
 */
public class HelloxJob implements SimpleJob{
    private static final Logger logger = LoggerFactory.getLogger(HelloxJob.class);
    private static final String dsUrlOne = "http://localhost:8081/hellox?name=";
    @Override
    public void execute(ShardingContext shardingContext) {
//        logger.info("job {}, param: {}, total sharding: {}, current sharding: {}", shardingContext.getJobName(), shardingContext.getShardingParameter(), shardingContext.getShardingTotalCount(), shardingContext.getShardingItem());
        String url = dsUrlOne + shardingContext.getShardingParameter();
        logger.info("test hellox request url {}", url);
        HttpUtils.urlParamGet(url, 2);
    }
}
