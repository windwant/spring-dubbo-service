package org.windwant.spring.core.spi.service;

import org.windwant.spring.core.spi.Calc;

/**
 * Created by Administrator on 2018/1/24.
 */
public class SinCalc implements Calc {
    @Override
    public Object calc(int calcType) {
        Object value = Math.sin(calcType);
        logger.info("sin result: {}", value);
        return value;
    }
}
