package org.windwant.serverx.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.windwant.serverx.core.mybatis.DataSource;

/**
 * Created by windwant on 2016/12/30.
 */
@Repository
@DataSource(DataSource.Type.LOCAL)
public interface LocalMapper {

    @Update("update student set level = #{level} where id = #{stuId}")
    int modifyLevel(@Param("stuId") int stuId, @Param("level") String level);

    @Select("select GROUP_CONCAT(score) from score s " +
            "left join subject sub on s.sub_id = sub.id " +
            "where stu_id = ${stuId}")
    String getScore(@Param("stuId") int stuId);
}
