package org.windwant.spring.core.spi;

import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by Administrator on 2018/1/24.
 */
@Component
public class SpiService {

    public Object execCalc(int value){
        ServiceLoader<Calc> loader = ServiceLoader.load(Calc.class);
        Iterator<Calc> iterator = loader.iterator();
        while (iterator.hasNext()) {
            return iterator.next().calc(value);
        }
        return null;
    }

    public static void main(String[] args) {
        new SpiService().execCalc(100);
    }
}
