package org.windwant.spring.model;

/**
 * Created by Administrator on 18-5-4.
 */
public class Score {
    private Integer id;

    private Integer stuId;

    private String item;

    private Double score;

    private Stu stu;

    public Stu getStu() {
        return stu;
    }

    public void setStu(Stu stu) {
        this.stu = stu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
