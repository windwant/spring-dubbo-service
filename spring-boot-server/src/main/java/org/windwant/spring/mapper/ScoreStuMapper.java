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
import org.windwant.spring.model.Stu;

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
    @Select("select id, name, sex from stu where id = #{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "sex", column = "sex", typeHandler = SexEnumHandler.class)//处理枚举型
    })
    Stu selectStuById(@Param("id") Integer id);

    /**
     * 查询 分数信息
     * @param id
     * @return
     */
//    @Select("select s.id, s.item, s.score, stu.id stuId, stu.`name` from score s, stu where s.stu_id = stu.id and s.id = 1")
    @Select("select id, stu_id, item, score from score where id = #{id}")
    @Results({
            @Result(id = true,property = "id" ,column = "id"),
            @Result(property ="item",column = "item",  typeHandler = MyStringTypeHandler.class),
            @Result(property ="score",column="score"),
            @Result(property ="stu",column="stu_id" //column 传入级联查询的参数
                    ,one =@One(select ="org.windwant.spring.mapper.ScoreStuMapper.selectStuById", fetchType = FetchType.LAZY))}
    )
    Score selectScoreById(@Param("id") Integer id);

}
