package org.windwant.spring.service;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.windwant.spring.core.mybatis.interceptor.Page;
import org.windwant.spring.model.Person;
import org.windwant.spring.model.Score;
import org.windwant.spring.model.Student;
import org.windwant.spring.model.User;
import org.windwant.spring.mongo.MongoPersonRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 18-7-23.
 */
@RunWith(MockitoJUnitRunner.class)
public class BootServiceTest {

    @Mock
    public BootService bootService;

    @Mock
    public MongoPersonRepository mongoService;

    public Page page1 = new Page(1);
    public Page page2 = new Page(2);
    public Page page3 = new Page(3);

    public User user = User.build("lilei", "123", 1);

    public Person p = new Person();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        Score score = new Score();
        score.setId(1);
        score.setStuId(1);
        score.setSubId(1);
        when(bootService.getScoreById(1, 0)).thenReturn(score);

        Student stu = new Student();
        stu.setId(1);
        stu.setName("lilei");
        when(bootService.getStuById(1, 0)).thenReturn(stu);

        when(bootService.getStu(page1)).thenReturn(getPageStudent(page1));
        when(bootService.getStu(page2)).thenReturn(getPageStudent(page2));
        when(bootService.getStu(page3)).thenReturn(getPageStudent(page3));

        when(bootService.login(user)).thenReturn(new HashMap(){{put("success", user);}});

        when(mongoService.findByLastName("lilei")).thenReturn(new ArrayList<Person>() {{
            for (int i = 0; i < 10; i++) {
                Person p = new Person();
                p.setFirstName("first_name" + i);
                p.setLastName("last_name" + i);
                add(p);
            }
        }});
        p.setFirstName("roger");
        p.setLastName("root");
        when(mongoService.insert(p)).thenReturn(p);
    }

    private List<Student> getPageStudent(Page page){
        List<Student> students = new ArrayList<>();
        if(page == null) return students;
        for (int i = 0; i < page.getLimit(); i++) {
            Student tmp = new Student();
            Integer id = page.getOffset() + i + 1;
            tmp.setId(id);
            tmp.setName("name" + id);
            students.add(tmp);
        }
        return students;
    }

    @Test
    public void testGetScoreById(){
        System.out.println(ToStringBuilder.reflectionToString(bootService.getScoreById(1, 0)));
    }

    @Test
    public void testGetStuById(){
        System.out.println(ToStringBuilder.reflectionToString(bootService.getStuById(1, 0)));
    }

    @Test
    public void testGetStu(){
        //the first page
        bootService.getStu(page1).forEach(item -> System.out.println(ToStringBuilder.reflectionToString(item)));
        //the second page
        bootService.getStu(page2).forEach(item -> System.out.println(ToStringBuilder.reflectionToString(item)));
        //the third page
        bootService.getStu(page3).forEach(item -> System.out.println(ToStringBuilder.reflectionToString(item)));
    }

    @Test
    public void testLogin(){
        System.out.println(bootService.login(user).toString());
    }

    @Test
    public void testMongo(){
        mongoService.findByLastName("lilei").forEach(item -> System.out.println(ToStringBuilder.reflectionToString(item)));
        System.out.println(ToStringBuilder.reflectionToString(mongoService.insert(p)));
    }
}
