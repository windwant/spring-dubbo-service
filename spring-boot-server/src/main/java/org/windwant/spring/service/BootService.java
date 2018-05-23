package org.windwant.spring.service;

import org.windwant.spring.core.mybatis.interceptor.Page;
import org.windwant.spring.model.*;

import java.util.List;
import java.util.Map;

/**
 * BootService.
 */
public interface BootService {

    String hello(String name);

    String hellox(Guest guest);

    Map<String, Object> login(User user);

    void testMongo();

    Score getScoreById(int id, int type);

    Student getStuById(int id, int type);

    List<Student> getStu(Page page);
}
