package org.windwant.serverx.core.mybatis.handler;


import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.StringTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理String null 参数
 * Created by Administrator on 18-5-14.
 */
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MyStringTypeHandler extends StringTypeHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyStringTypeHandler.class);
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null) parameter = "";
        logger.info("setNonNullParameter PreparedStatement: {}", parameter);
        super.setNonNullParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
            String result = super.getNullableResult(rs, columnName);
        logger.info("getNullableResult ResultSet: {}", result);
        return result == null?"":result;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = super.getNullableResult(rs, columnIndex);
        logger.info("getNullableResult ResultSet: {}", result);
        return result == null?"":result;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = super.getNullableResult(cs, columnIndex);
        logger.info("getNullableResult CallableStatement: {}", result);
        return result == null?"":result;
    }
}
