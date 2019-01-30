package org.windwant.spring.service.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.windwant.common.api.DubboRestService;
import org.windwant.common.api.model.Guest;
import org.windwant.common.api.model.Person;
import org.windwant.common.api.model.Score;
import org.windwant.common.api.model.Student;
import org.windwant.spring.core.mybatis.interceptor.Page;
import org.windwant.spring.mapper.*;
import org.windwant.spring.mapper.xmlmapper.ScoreStuXmlMapper;
import org.windwant.spring.mapper.xmlmapper.StuScoreXmlMapper;
import org.windwant.spring.mongo.MongoPersonRepository;
import org.windwant.spring.util.LangUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 通过dubbo rpc访问
 * Created by Administrator on 2018/2/6.
 */
@Service("dubborestsvr")
public class DubboRestServiceImpl implements DubboRestService {
    private static final Logger logger = LoggerFactory.getLogger(DubboRestServiceImpl.class);

    @Autowired
    LangUtil langUtil;

    @Autowired
    LocalMapper localMapper;
    @Autowired
    RemoteMapper remoteMapper;

    @Override
    public String hello(String name){
        String message = "Hello " + name + ", welcome to my world!";
        logger.info(message);
        String source = localMapper.getResult(1);
        logger.info("mapper from @: {}", localMapper.getStringResult(1));
        logger.info("mapper from xml: {}", source);

        logger.info("data from {}", source);
        return message + " from " + source;
    }

    public String hellox(Guest guest){
        String message = "Hellox " + guest.getName() + ", welcome to my world!";
        logger.info(message);
        logger.info("guest: {}", guest.toString());
        String source = remoteMapper.getResult(1);
        logger.info("mapper from @: {}", remoteMapper.getStringResult(1));
        logger.info("mapper from xml: {}", source);
        logger.info("data from {}", source);
        return message + " from " + source;
    }

    @Autowired
    private MongoPersonRepository personRepository;

    public void testMongo(){
        Person person = new Person();
        person.setFirstName(String.valueOf("F" + ThreadLocalRandom.current().nextInt(100)));
        person.setLastName(String.valueOf("L" + ThreadLocalRandom.current().nextInt(100)));
        personRepository.insert(person);
        personRepository.findAll().forEach(item -> logger.info("mongo item: {}", item.toString()));
    }

    @Autowired
    ScoreStuMapper scoreStuMapper;

    @Autowired
    StuScoreMapper stuScoreMapper;

    @Autowired
    ScoreStuXmlMapper scoreStuXmlMapper;

    @Autowired
    StuScoreXmlMapper stuScoreXmlMapper;

    @Override
    public Score getScoreById(int id, int type) {
        Score score;
        //注解mapper
        if(type == 0){
            score = scoreStuMapper.selectScoreById(id);
            logger.info("annotation mapper score: {}", ToStringBuilder.reflectionToString(score));
        }else {
            score = scoreStuXmlMapper.selectScoreByIdXML(id);
            logger.info("xml mapper score: {}", ToStringBuilder.reflectionToString(score));
            score = scoreStuXmlMapper.selectScoreByIdXMLX(id);
            logger.info("xml mapper join search score: {}", ToStringBuilder.reflectionToString(score));
        }
        return score;
    }

    /**
     * 事务应用 需要新事务传播级别 可重复读隔离级别
     * @param id
     * @param type
     * @return
     */
    @Transactional(transactionManager = "txMgr", propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    @Override
    public Student getStuById(int id, int type) {
        Student stu;
        //注解mapper
        if(type == 0){
            stu = stuScoreMapper.selectStuById(id);
            logger.info("annotation mapper stu: {}", ToStringBuilder.reflectionToString(stu));
        }else {
            stu = stuScoreXmlMapper.selectStuByIdXML(id);
            logger.info("xml mapper stu: {}", ToStringBuilder.reflectionToString(stu));
            stu = stuScoreXmlMapper.selectStuByIdXMLX(id);
            logger.info("xml mapper join search stu: {}", ToStringBuilder.reflectionToString(stu));
        }
        return stu;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Student> getStu(int page) {
        List<Student> stus = stuScoreMapper.selectStu(new Page(page));
        ((DubboRestService) AopContext.currentProxy()).getStuById(1, 0); //exposeProxy = true目标对象内部的自我调用的事务增强支持 同时改为此种调用方式
        return stus;
    }
}
