package org.windwant.spring.core.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class MapperFactoryBeanProxy implements FactoryBean {

    private Class<?> mapperInterface;

    private MapperFactoryBean mapperFactoryBean;

    public MapperFactoryBeanProxy(Class<?> mapperInterface) {
        this.mapperInterface = mapperInterface;
        mapperFactoryBean = new MapperFactoryBean<>(mapperInterface);
    }

    @Override
    public Object getObject() throws Exception {
        mapperFactoryBean.afterPropertiesSet();
        Object object = mapperFactoryBean.getObject();
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[]{mapperFactoryBean.getMapperInterface()},
                new MapperProxy(object, mapperInterface));
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public boolean isAddToConfig() {
        return mapperFactoryBean.isAddToConfig();
    }

    public void setAddToConfig(boolean addToConfig) {
        mapperFactoryBean.setAddToConfig(addToConfig);
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        mapperFactoryBean.setSqlSessionTemplate(sqlSessionTemplate);
    }
}
