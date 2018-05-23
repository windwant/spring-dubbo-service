package org.windwant.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.windwant.spring.core.enums.Sex;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 18-5-4.
 */
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"})
public class Student implements Serializable {

    private Integer id;

    private String name;

    private Sex sex;

    private List<Score> scores;

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
