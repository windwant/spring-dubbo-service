package org.windwant.spring.core.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    enum Type {
        LOCAL,
        REMOTE
    }

    Type value();
}
