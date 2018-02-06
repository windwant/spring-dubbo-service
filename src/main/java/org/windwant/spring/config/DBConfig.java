package org.windwant.spring.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
 * implements EnvironmentAware, ApplicationContextAware
 */
@Configuration
public class DBConfig {

//    private Environment environment;
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.environment = environment;
//    }

    @Primary
    @Bean(name = "localDataSource")
    @Order(value = 1)
    @ConfigurationProperties(prefix = "datasource.local")
    public DataSource localDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Order(value = 2)
    @Bean(name = "remoteDataSource")
    @ConfigurationProperties(prefix = "datasource.remote")
    public DataSource remoteDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "routingDataSource")
    @Order(value = 3)
    public DataSource routingDataSource(@Qualifier("localDataSource") DataSource localDataSource,
                                        @Qualifier("remoteDataSource") DataSource remoteDataSource){
        RoutingDataSource routingDataSource = new RoutingDataSource();
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(Type.LOCAL.name(), localDataSource);
        dataSources.put(Type.REMOTE.name(), remoteDataSource);
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(localDataSource);
        return routingDataSource;
    }


    @Bean
    @Order(value = 4)
    @Lazy
    public SqlSessionFactory sqlSessionFactory(@Qualifier("remoteDataSource") DataSource remoteDataSource,
                                               @Qualifier("localDataSource") DataSource localDataSource,
                                               @Qualifier("routingDataSource") DataSource routingDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(routingDataSource);
        factoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/*.xml"));
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

//    private ApplicationContext ctx;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.ctx = applicationContext;
//    }
}
