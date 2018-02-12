package org.windwant.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.util.HttpUtils;

/**
 * Created by Administrator on 2018/2/12.
 */
public class HelloJob implements SimpleJob{
    private static final Logger logger = LoggerFactory.getLogger(HelloJob.class);
    private static final String dsUrlTwo = "http://localhost:8081/hello/";
    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("job {}, param: {}", shardingContext.getJobName(), shardingContext.getShardingParameter());
        String url = dsUrlTwo + shardingContext.getShardingParameter();
        logger.info("test hello request url {}", url);
        HttpUtils.urlParamGet(url, 2);
    }
}
