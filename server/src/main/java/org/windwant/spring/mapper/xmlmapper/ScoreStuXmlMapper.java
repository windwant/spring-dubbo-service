package org.windwant.spring.mapper.xmlmapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.windwant.common.api.model.Score;
import org.windwant.spring.core.mybatis.DataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;

/**
 * Created by windwant on 2016/12/30.
 * 一对一级联查询
 */
@Repository
@DataSource(Type.LOCAL)
public interface ScoreStuXmlMapper {
    /**
     * xml mapper 配置查询分数
     * @param id
     * @return
     */
    Score selectScoreByIdXML(@Param("id") Integer id);

    /**
     * 另一种级联
     * @param id
     * @return
     */
    Score selectScoreByIdXMLX(@Param("id") Integer id);
}
