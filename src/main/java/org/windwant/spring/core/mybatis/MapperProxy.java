package org.windwant.spring.core.mybatis;

import org.apache.ibatis.reflection.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.windwant.spring.core.datasource.RoutingDataSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapperProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(MapperProxy.class);

    private Object target;

    private Class<?> mapperInterface;

    public MapperProxy(Object target, Class<?> mapperInterface) {
        this.target = target;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Object 中定义的方法不需要拦截
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw ExceptionUtil.unwrapThrowable(t);
            }
        }

        // set routing data source
        DataSource annotation = mapperInterface.getAnnotation(DataSource.class);
        if (annotation == null) {
            RoutingDataSource.setThreadLocalDataSourceKey(null);
        } else {
            RoutingDataSource.setThreadLocalDataSourceKey(annotation.value().name());
        }

        Object object = method.invoke(target, args);

        // reset routing datasource
        if (annotation != null) {
            RoutingDataSource.setThreadLocalDataSourceKey(null);
        }

        return object;
    }

    private boolean isBlank(Object object) {
        return object == null || object instanceof Collection && ((Collection) object).isEmpty();
    }

    private String getKey(String prefix, Object[] args) {
        return Stream.of(args).map(Object::toString).collect(Collectors.joining(":", prefix, ""));
    }

    private void dumpInvoke(String prefix, Method method, Object[] args, String result) {
        final String clazz = method.getDeclaringClass().getSimpleName();
        final String name = method.getName();
        logger.info(Stream.of(args).map(Object::toString).collect(Collectors.joining(" ",
                prefix + " " + clazz + "." + name + " ", " " + result)));
    }
}
