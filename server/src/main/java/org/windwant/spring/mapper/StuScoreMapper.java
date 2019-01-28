package org.windwant.spring.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.mapping.ResultSetType;
import org.mybatis.caches.ehcache.EhcacheCache;
import org.springframework.stereotype.Repository;
import org.windwant.common.api.model.Score;
import org.windwant.common.api.model.Student;
import org.windwant.spring.core.mybatis.DataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;
import org.windwant.spring.core.mybatis.handler.SexEnumHandler;
import org.windwant.spring.core.mybatis.interceptor.Page;

import java.util.List;

/**
 * Created by windwant on 2016/12/30.
 * 一对多级联查询
 */
@Repository
@DataSource(Type.LOCAL)
@CacheNamespace(implementation = EhcacheCache.class, eviction = LruCache.class, flushInterval = 10000, size = 1024)//二级缓存配置
public interface StuScoreMapper {

    /**
     * 查询学生信息
     *
     * @param id
     * @return
     */
    @Select("select id, name, sex from student where id = #{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "sex", column = "sex", typeHandler = SexEnumHandler.class),//处理枚举型
            @Result(property = "score", column = "score"),
            @Result(property = "scores", column = "id"  //column 传入级联查询的参数
                    , many = @Many(select = "org.windwant.spring.mapper.StuScoreMapper.selectScoreById", fetchType = FetchType.LAZY))}
    )
    @Options(useCache = true, flushCache = Options.FlushCachePolicy.DEFAULT, timeout = 10000, fetchSize = 1024, resultSetType = ResultSetType.SCROLL_SENSITIVE)
    Student selectStuById(@Param("id") Integer id);

    /**
     * 级联 查询分数信息
     *
     * @param id
     * @return
     */
    @Options(useCache = true, flushCache = Options.FlushCachePolicy.DEFAULT, timeout = 10000)
    @Select("select id, stu_id, sub_id, score from score where stu_id = #{id}")
    Score selectScoreById(@Param("id") Integer id);


    @Select("<script>" +
            "select id, name from student " +
            "limit #{offset,jdbcType=INTEGER},#{limit,jdbcType=INTEGER} " +
            "</script>")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)//处理分页查询缓存问题
    List<Student> selectStu(Page page);
}
