package org.windwant.serverx.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.windwant.common.api.DubboTXServicex;
import org.windwant.serverx.mapper.RemoteMapper;

/**
 * 通过dubbo rpc访问
 * Created by Administrator on 2018/2/6.
 */
@Service("dubbotxsvrx")
public class DubboTXServicexImpl implements DubboTXServicex {
    private static final Logger logger = LoggerFactory.getLogger(DubboTXServicexImpl.class);

    @Autowired
    JdbcTemplate localTemplate;

    @Autowired
    RemoteMapper remoteMapper;

    /**
     * 更新分数
     * @param stuId
     * @param item
     * @return
     */
    @Override
    public int downScore(int stuId, int item, int score) {
//        return localTemplate.update("update score set score = ? where stu_id = ? and sub_id = ?", score, stuId, item);
        int sscore = remoteMapper.getScoreByStuId(stuId, item);

        int result = sscore - score;
        return remoteMapper.downScoreByStuId(stuId, result > 0 ? result : 0, item);
    }
}
