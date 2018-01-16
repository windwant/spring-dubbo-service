package org.windwant.spring.core.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by windwant on 2016/12/30.
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> dataSourceKey = new ThreadLocal<>();

    public static void setThreadLocalDataSourceKey(String dataSourceKey){
        RoutingDataSource.dataSourceKey.set(dataSourceKey);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDataSource.dataSourceKey.get();
    }
}
