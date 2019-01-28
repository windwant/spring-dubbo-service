package org.windwant.spring.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.windwant.common.api.model.User;
import org.windwant.spring.core.mybatis.DataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;

/**
 * Created by windwant on 2016/12/30.
 */
@Repository
@DataSource(Type.LOCAL)
public interface UserMapper {

    @Select("select #{userName} as userName, '123456' as passwd, '1234' as code ")
    User getUserByUserName(@Param("userName") String userName);
}
