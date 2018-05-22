package org.windwant.spring.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.*;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.windwant.spring.core.datasource.RoutingDataSource;
import org.windwant.spring.core.mybatis.DataSource.Type;
import org.windwant.spring.core.mybatis.MapperScannerConfigurerProxy;
import org.windwant.spring.core.mybatis.interceptor.PageIntercept;

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

    /**
     * mybatis mapper 扫描
     * @return
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurerProxy mapperScannerConfigurerProxy = new MapperScannerConfigurerProxy();
        mapperScannerConfigurerProxy.setBasePackage("org.windwant.spring.mapper");
        return mapperScannerConfigurerProxy;
    }

    /**
     * mybatis 自定义 typeHandler
     * @return
     */
//    @Bean
//    public TypeHandlerRegistry typeHandlerRegistry(){
//        TypeHandlerRegistry registry = new TypeHandlerRegistry();
//        registry.register("org.windwant.spring.core.mybatis.handler");
//        return registry;
//    }

    /**
     * mybatis 配置
     */
    @Bean
    public org.apache.ibatis.session.Configuration configuration(){
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        //延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置fetchType属性来覆盖该项的开关状态。
        config.setLazyLoadingEnabled(true);
        config.setAggressiveLazyLoading(false);
        //应用缓存
        config.setCacheEnabled(true);
        //配置默认的执行器。SIMPLE 就是普通的执行器；REUSE 执行器会重用预处理语句（prepared statements）； BATCH 执行器将重用语句并执行批量更新。
        config.setDefaultExecutorType(ExecutorType.BATCH);
        //当没有为参数提供特定的 JDBC 类型时，为空值指定 JDBC 类型。 某些驱动需要指定列的 JDBC 类型，多数情况直接用一般类型即可，比如 NULL、VARCHAR 或 OTHER。
        config.setJdbcTypeForNull(JdbcType.NULL);
        //缓存范围
        config.setLocalCacheScope(LocalCacheScope.SESSION);
        //指定 MyBatis 应如何自动映射列到字段或属性。 NONE 表示取消自动映射；PARTIAL 只会自动映射没有定义嵌套结果集映射的结果集。 FULL 会自动映射任意复杂的结果集（无论是否嵌套）
        config.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        //是否允许单一语句返回多结果集（需要兼容驱动）
        config.setMultipleResultSetsEnabled(true);
        //是否开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射。
        config.setMapUnderscoreToCamelCase(true);
        //设置超时时间，它决定驱动等待数据库响应的秒数。
        config.setDefaultStatementTimeout(5);
        //Sets the driver a hint as to control fetching size for return results. This parameter value can be override by a query setting.
        config.setDefaultFetchSize(1024 * 10);
        //未知列映射行为
        config.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.NONE);
        //拦截器配置
        config.addInterceptor(new PageIntercept());
        //type alias package 类型映射
        config.getTypeAliasRegistry().registerAliases("org.windwant.spring.model");
        //mybatis 自定义 typeHandler
        config.getTypeHandlerRegistry().register("org.windwant.spring.core.mybatis.handler");
        return config;
    }

    @Bean
    @Order(value = 4)
    @Lazy
    public SqlSessionFactory sqlSessionFactory(@Qualifier("remoteDataSource") DataSource remoteDataSource,
                                               @Qualifier("localDataSource") DataSource localDataSource,
                                               @Qualifier("routingDataSource") DataSource routingDataSource,
                                               @Qualifier("configuration") org.apache.ibatis.session.Configuration configuration) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(routingDataSource);
        //驼峰映射
//        factoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        //xml mapper文件
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/*.xml"));
        factoryBean.setConfiguration(configuration);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean(name = "txMgr")
    public DataSourceTransactionManager transactionManager(@Qualifier("routingDataSource") DataSource routingDataSource){
        DataSourceTransactionManager mgr = new DataSourceTransactionManager(routingDataSource);
        return mgr;
    }

//    private ApplicationContext ctx;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.ctx = applicationContext;
//    }
}
