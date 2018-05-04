package org.windwant.spring.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.windwant.spring.core.mybatis.DataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;
import org.windwant.spring.model.Score;
import org.windwant.spring.model.Stu;

import java.util.List;

/**
 * Created by windwant on 2016/12/30.
 * 一对多级联查询
 */
@Repository
@DataSource(Type.LOCAL)
public interface StuScoreMapper {

    @Select("select id, name from stu where id = #{id}")
    @Results({
            @Result(id = true,property = "id" ,column = "id"),
            @Result(property ="item",column = "item"),
            @Result(property ="score",column="score"),
            @Result(property ="scores",column="id"
                    ,many = @Many(select ="org.windwant.spring.mapper.StuScoreMapper.selectScoreById"))}
    )
    Stu selectStuById(@Param("id") Integer id);

    @Select("select id, stu_id, item, score from score where stu_id = #{id}")
    Score selectScoreById(@Param("id") Integer id);

    Stu selectStuByIdXML(@Param("id") Integer id);

//    List<Score> selectScoreByIdXML(@Param("id") Integer id);
}
