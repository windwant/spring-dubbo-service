package org.windwant.spring.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.mapping.FetchType;
import org.mybatis.caches.ehcache.EhcacheCache;
import org.springframework.stereotype.Repository;
import org.windwant.spring.core.mybatis.DataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;
import org.windwant.spring.core.mybatis.handler.MyStringTypeHandler;
import org.windwant.spring.core.mybatis.handler.SexEnumHandler;
import org.windwant.spring.model.Score;
import org.windwant.spring.model.Student;
import org.windwant.spring.model.Subject;

/**
 * Created by windwant on 2016/12/30.
 * 一对一级联查询
 */
@Repository
@DataSource(Type.LOCAL)
@CacheNamespace(implementation = EhcacheCache.class, eviction = LruCache.class, flushInterval = 10000, size = 1024)//二级缓存配置
public interface ScoreStuMapper {

    /**
     * 分数 级联查询 学生信息
     * @param id
     * @return
     */
    @Select("select id, name, sex from student where id = #{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name", typeHandler = MyStringTypeHandler.class),
            @Result(property = "sex", column = "sex", typeHandler = SexEnumHandler.class)//处理枚举型
    })
    Student selectStuById(@Param("id") Integer id);

    /**
     * 科目 级联查询
     * @param id
     * @return
     */
    @Select("select id, name from subject where id = #{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name", typeHandler = MyStringTypeHandler.class)
    })
    Subject selectSubjectById(@Param("id") Integer id);

    /**
     * 查询 分数信息
     * @param id
     * @return
     */
    @Select("select id, stu_id, sub_id, score from score where id = #{id}")
    @Results({
            @Result(id = true,property = "id" ,column = "id"),
            @Result(property ="score",column="score"),
            @Result(property ="stuId",column="stu_id"),
            @Result(property ="subId",column="sub_id"),
            @Result(property ="subject",column="sub_id" //column 传入级联查询的参数
                    ,one =@One(select ="org.windwant.spring.mapper.ScoreStuMapper.selectSubjectById", fetchType = FetchType.LAZY)),
            @Result(property ="student",column="stu_id" //column 传入级联查询的参数
                    ,one =@One(select ="org.windwant.spring.mapper.ScoreStuMapper.selectStuById", fetchType = FetchType.LAZY))}
    )
    Score selectScoreById(@Param("id") Integer id);

}
