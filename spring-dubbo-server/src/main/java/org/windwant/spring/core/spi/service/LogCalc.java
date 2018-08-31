package org.windwant.spring.core.spi.service;

import org.windwant.spring.core.spi.Calc;

/**
 * Created by Administrator on 2018/1/24.
 */
public class LogCalc implements Calc {
    @Override
    public Object calc(int calcType) {
        Object value = Math.log10(calcType);
        logger.info("log10 result: {}", value);
        return value;
    }
}
