package org.windwant.common.api;

import org.windwant.common.api.model.Guest;
import org.windwant.common.api.model.Score;
import org.windwant.common.api.model.Student;

import java.util.List;

/**
 * DubboRestService.
 */
public interface DubboRestService {

    String hello(String name);

    String hellox(Guest guest);

    void testMongo();

    Score getScoreById(int id, int type);

    Student getStuById(int id, int type);

    List<Student> getStu(int page);
}
