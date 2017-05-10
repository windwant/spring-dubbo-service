package org.windwant.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.windwant.spring.mapper.MySelMapper;
import org.windwant.spring.service.BootService;

/**
 * BootServiceImpl
 */
@Service
public class BootServiceImpl implements BootService {

    @Autowired
    MySelMapper mySelMapper;

    @Override
    public String hello(String name){
        String message = "Hello " + name + ", welcome to my world!";
        System.out.println(message);
        System.out.println("mapper from @: " + mySelMapper.getStringResult(1));
        System.out.println("mapper from xml: " + mySelMapper.getResult(1));
        return message;
    }
}
