package org.windwant.common.api;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @param
 */
public class SerializationOptimizerImpl implements SerializationOptimizer {


    @SuppressWarnings("rawtypes")
	@Override
    public Collection<Class> getSerializableClasses() {
        List<Class> classes = new LinkedList<>();
        classes.add(String.class);
        return classes;
    }
}
