package org.windwant.spring.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.windwant.spring.mybatis.DataSource;
import org.windwant.spring.mybatis.DataSource.Type;

/**
 * Created by windwant on 2016/12/30.
 */
@Repository
@DataSource(Type.REMOTE)
public interface MySelRMapper {

    @Select("select USER()")
    String getStringResult(@Param("id") Integer id);

    String getResult(@Param("id") Integer id);
}
