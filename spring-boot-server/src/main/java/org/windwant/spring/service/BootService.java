package org.windwant.spring.service;

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

    Score getScoreById(int id);

    Stu getStuById(int id);
}
