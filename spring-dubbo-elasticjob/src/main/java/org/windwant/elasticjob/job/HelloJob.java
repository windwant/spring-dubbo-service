package org.windwant.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.common.util.HttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Administrator on 2018/2/12.
 */
public class HelloJob implements DataflowJob{
    private static final Logger logger = LoggerFactory.getLogger(HelloJob.class);
    private static final String dsUrlTwo = "http://localhost:8081/hello/";

    @Override
    public List fetchData(ShardingContext shardingContext) {
        return new ArrayList(){{
            add(ThreadLocalRandom.current().nextInt(5));
        }};
    }

    @Override
    public void processData(ShardingContext shardingContext, List data) {
        logger.info("job {}, param: {}", shardingContext.getJobName(), shardingContext.getShardingParameter());
        String url = dsUrlTwo + shardingContext.getShardingParameter() + data.get(0);
        logger.info("test hello request url {}", url);
        HttpUtils.urlParamGet(url, 2);
    }
}
