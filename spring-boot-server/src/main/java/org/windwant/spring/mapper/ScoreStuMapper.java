package org.windwant.spring.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;
import org.windwant.spring.core.enums.Sex;
import org.windwant.spring.core.mybatis.DataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;
import org.windwant.spring.core.mybatis.handler.MyStringTypeHandler;
import org.windwant.spring.core.mybatis.handler.SexEnumHandler;
import org.windwant.spring.model.Score;
import org.windwant.spring.model.Stu;

import java.util.List;

/**
 * Created by windwant on 2016/12/30.
 * 一对一级联查询
 */
@Repository
@DataSource(Type.LOCAL)
public interface ScoreStuMapper {

    @Select("select id, name, sex from stu where id = #{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "sex", column = "sex", typeHandler = SexEnumHandler.class)//处理枚举型
    })
    Stu selectStuById(@Param("id") Integer id);

//    @Select("select s.id, s.item, s.score, stu.id stuId, stu.`name` from score s, stu where s.stu_id = stu.id and s.id = 1")
    @Select("select id, stu_id, item, score from score where id = #{id}")
    @Results({
            @Result(id = true,property = "id" ,column = "id"),
            @Result(property ="item",column = "item",  typeHandler = MyStringTypeHandler.class),
            @Result(property ="score",column="score"),
            @Result(property ="stu",column="stu_id"
                    ,one =@One(select ="org.windwant.spring.mapper.ScoreStuMapper.selectStuById"))}
    )
    Score selectScoreById(@Param("id") Integer id);

    Score selectScoreByIdXML(@Param("id") Integer id);
}
