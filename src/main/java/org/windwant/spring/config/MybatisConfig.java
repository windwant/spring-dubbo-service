package org.windwant.spring.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.windwant.spring.datasource.RoutingDataSource;
import org.windwant.spring.mybatis.DataSource.Type;
import org.windwant.spring.mybatis.MapperScannerConfigurerProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by windwant on 2016/12/30.
 */
public class MybatisConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Primary
    @Bean
    public DataSource localDatasource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClass"));
        dataSource.setUrl(environment.getProperty("local.jdbc.url"));
        dataSource.setUsername(environment.getProperty("local.jdbc.user"));
        dataSource.setPassword(environment.getProperty("local.jdbc.password"));
        return dataSource;
    }

    @Bean
    public DataSource remoteDatasource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClass"));
        dataSource.setUrl(environment.getProperty("remote.jdbc.url"));
        dataSource.setUsername(environment.getProperty("remote.jdbc.user"));
        dataSource.setPassword(environment.getProperty("remote.jdbc.password"));
        return dataSource;
    }

    @Bean
    public DataSource routingDataSource(DataSource localDatasource, DataSource remoteDatasource){
        RoutingDataSource routingDataSource = new RoutingDataSource();
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(Type.LOCAL.name(), localDatasource);
        dataSources.put(Type.REMOTE.name(), remoteDatasource);
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(localDatasource);
        return routingDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource routingDataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(routingDataSource);
        factoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/*.xml"));
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurerProxy configurer = new MapperScannerConfigurerProxy();
        configurer.setBasePackage("org.windwant.spring.mapper");
        return configurer;
    }
}
