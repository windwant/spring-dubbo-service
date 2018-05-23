package org.windwant.spring.mapper.xmlmapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.windwant.spring.core.mybatis.DataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;
import org.windwant.spring.model.Student;

/**
 * Created by windwant on 2016/12/30.
 * 一对多级联查询
 */
@Repository
@DataSource(Type.LOCAL)
public interface StuScoreXmlMapper {

    /**
     * xml mapper 查询学生信息
     * @param id
     * @return
     */
    Student selectStuByIdXML(@Param("id") Integer id);

    /**
     * 另一种级联
     * @param id
     * @return
     */
    Student selectStuByIdXMLX(@Param("id") Integer id);
}
