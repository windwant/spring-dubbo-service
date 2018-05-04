package org.windwant.spring.model;

import java.util.List;

/**
 * Created by Administrator on 18-5-4.
 */
public class Stu {

    private Integer id;

    private String name;

    private List<Score> scores;

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
