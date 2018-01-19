package org.windwant.spring.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.windwant.spring.core.datasource.RoutingDataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by windwant on 2016/12/30.
 */
@Configuration
@MapperScan(basePackages = "org.windwant.spring.mapper")
public class MybatisConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "datasource.local")
    @Order(value = 1)
    public DataSource localDatasource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.remote")
    @Order(value = 2)
    public DataSource remoteDatasource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Order(value = 3)
    public DataSource routingDataSource(DataSource localDatasource,
                                        DataSource remoteDatasource){
        RoutingDataSource routingDataSource = new RoutingDataSource();
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(Type.LOCAL.name(), localDatasource);
        dataSources.put(Type.REMOTE.name(), remoteDatasource);
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(localDatasource);
        return routingDataSource;
    }


    @Bean
    @Order(value = 4)
    public SqlSessionFactory sqlSessionFactory(DataSource routingDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(routingDataSource);
        factoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/*.xml"));
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
