package org.windwant.spring.core.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/1/24.
 */
public interface Calc {
    Logger logger = LoggerFactory.getLogger(Calc.class);

    Object calc(int calcType);
}
