package org.windwant.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.windwant.spring.mapper.MySelMapper;
import org.windwant.spring.mapper.MySelRMapper;
import org.windwant.spring.service.BootService;

/**
 * BootServiceImpl
 */
@Service
public class BootServiceImpl implements BootService {

    @Autowired
    MySelMapper mySelMapper;
    @Autowired
    MySelRMapper mySelRMapper;

    @Override
    public String hello(String name){
        String message = "Hello " + name + ", welcome to my world!";
        System.out.println(message);
        System.out.println("mapper from @: " + mySelMapper.getStringResult(1));
        System.out.println("mapper from xml: " + mySelMapper.getResult(1));
        return message;
    }

    public String hellox(String name){
        String message = "Hellox " + name + ", welcome to my world!";
        System.out.println(message);
        System.out.println("mapper from @: " + mySelRMapper.getStringResult(1));
        System.out.println("mapper from xml: " + mySelRMapper.getResult(1));
        return message;
    }
}
