package org.windwant.spring.core.spi.service;

import org.windwant.spring.core.spi.Calc;

/**
 * Created by Administrator on 2018/1/24.
 */
public class SqrtCalc implements Calc {
    @Override
    public Object calc(int calcType) {
        Object value = Math.sqrt(calcType);
        logger.info("sqrt result: {}", value);
        return value;
    }
}
