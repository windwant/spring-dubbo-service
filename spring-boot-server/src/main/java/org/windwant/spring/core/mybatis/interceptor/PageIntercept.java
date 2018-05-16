package org.windwant.spring.core.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/** 
*/
@Intercepts({
    @Signature(type=Executor.class,method="query",args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})
})
public class PageIntercept implements Interceptor {

	public static final Logger logger = LoggerFactory.getLogger(PageIntercept.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
        Object parameter = invocation.getArgs()[1];
        Object args = invocation.getArgs()[1];
		Object returnValue = invocation.proceed();
        if(args instanceof Page && ((Page) args).isCount()){
            MappedStatement mappedStatement=(MappedStatement)invocation.getArgs()[0];
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            String originalSql = boundSql.getSql().trim();
            Object parameterObject = boundSql.getParameterObject();
            if(parameterObject instanceof Page){
                ((Page) parameterObject).setOffset(0);
                ((Page) parameterObject).setPage(1);
                ((Page) parameterObject).setLimit(Integer.MAX_VALUE/((Page) parameterObject).getPage());
            }

            String countSql = "SELECT COUNT(*) FROM (" + originalSql + ") aliasForPage";
            Connection connection=mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();

            BoundSql countBS = copyFromBoundSql(mappedStatement, boundSql, countSql);
            DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, countBS);
            PreparedStatement countStmt = connection.prepareStatement(countSql);
            parameterHandler.setParameters(countStmt);
            ResultSet rs = countStmt.executeQuery();
            int total =0;
            if (rs.next()) {
                total = rs.getInt(1);
            }
            rs.close();
            countStmt.close();
            connection.close();
            //分页计算
            ((Page) args).setTotal(total);
        }

		return returnValue;
	}


    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                }
            }
        return newBoundSql;
    }



    @Override
	public Object plugin(Object target) {
		 return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
