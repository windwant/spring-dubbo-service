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
@DataSource(DataSource.Type.REMOTE)
public interface RemoteMapper {

    @Update("update score set score = #{score} where stu_id = #{stuId} and sub_id = ${item}")
    int downScoreByStuId(@Param("stuId") int stuId, @Param("score") int score, @Param("item") int item);

    @Select("select score from score  where stu_id = #{stuId} and sub_id = ${item}")
    int getScoreByStuId(@Param("stuId") int stuId, @Param("item") int item);
}
