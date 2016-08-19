package org.windwant.spring.service.impl;

import org.springframework.stereotype.Service;
import org.windwant.spring.service.BootService;

/**
 * Created by aayongche on 2016/8/19.
 */
@Service
public class BootServiceImpl implements BootService {

    @Override
    public String hello(String name){
        String message = "Hello " + name + ", welcome to my world!";
        System.out.println(message);
        return message;
    }
}
