package org.windwant.serverx.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.windwant.common.api.DubboTXServicey;
import org.windwant.serverx.mapper.LocalMapper;

import java.util.Map;

/**
 * 通过dubbo rpc访问
 * Created by Administrator on 2018/2/6.
 */
@Service("dubbotxsvry")
public class DubboTXServiceyImpl implements DubboTXServicey {
    private static final Logger logger = LoggerFactory.getLogger(DubboTXServiceyImpl.class);

    @Autowired
    JdbcTemplate remoteTemplate;

    @Autowired
    LocalMapper localMapper;

    /**
     * 更新级别
     * @param stuId
     * @return
     */
    @Override
    public int modifyLevel(int stuId) {
        logger.info("modify student level: {}", stuId);
        Map result = remoteTemplate.queryForMap("select GROUP_CONCAT(score) ss from score s left join subject sub on s.sub_id = sub.id  where stu_id = ?", stuId);
        String scoresConcat = String.valueOf(result.get("ss"));
        String[] scores = StringUtils.split(scoresConcat, ",");
        int num80 = 0;
        int num60 = 0;
        int num00 = 0;
        for (String score : scores) {
            int tmp = Integer.parseInt(score);
            if(tmp >= 80){
                num80++;
            }else if(tmp >= 60){
                num60++;
            }else {
                num00++;
            }
        }
        String level = "";
        if(num80 == 3) level = "A";
        else if(num80 == 2 && num60 == 1) level = "B";
        else if(num80 == 1 && num60 == 2) level = "C";
        else if(num60 == 3) level = "D";
        else if(num00 == 3) level = "F";
        else level = "E";

//        return remoteTemplate.update("update student set level = 'e' where id = 1");

        return localMapper.modifyLevel(stuId, level);
    }
}
