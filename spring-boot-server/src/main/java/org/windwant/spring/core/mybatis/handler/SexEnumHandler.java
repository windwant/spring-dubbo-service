package org.windwant.spring.core.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.windwant.spring.core.enums.Sex;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 18-5-14.
 */
@MappedTypes(Sex.class)
public class SexEnumHandler extends BaseTypeHandler<Sex> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Sex parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public Sex getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Sex.getSex(rs.getInt(columnName));
    }

    @Override
    public Sex getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Sex.getSex(rs.getInt(columnIndex));
    }

    @Override
    public Sex getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Sex.getSex(cs.getInt(columnIndex));
    }
}
