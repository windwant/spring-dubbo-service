package org.windwant.spring.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.windwant.spring.mapper.MySelMapper;
import org.windwant.spring.mapper.MySelRMapper;
import org.windwant.spring.model.Guest;
import org.windwant.spring.service.BootService;

/**
 * BootServiceImpl
 */
@Service
public class BootServiceImpl implements BootService {
    private static final Logger logger = LoggerFactory.getLogger(BootServiceImpl.class);

    @Autowired
    MySelMapper mySelMapper;
    @Autowired
    MySelRMapper mySelRMapper;

    @Override
    public String hello(String name){
        String message = "Hello " + name + ", welcome to my world!";
        logger.info(message);
        logger.info("mapper from @: {}", mySelMapper.getStringResult(1));
        logger.info("mapper from xml: ", mySelMapper.getResult(1));
        return message;
    }

    public String hellox(Guest guest){
        String message = "Hellox " + guest.getName() + ", welcome to my world!";
        logger.info(message);
        logger.info("guest: {}", guest.toString());
        logger.info("mapper from @: {}", mySelRMapper.getStringResult(1));
        logger.info("mapper from xml: {}", mySelRMapper.getResult(1));
        return message;
    }
}
