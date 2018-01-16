package org.windwant.spring.model;

import org.hibernate.validator.constraints.NotBlank;
import org.windwant.spring.util.DateUtil;

/**
 * Created by Administrator on 2018/1/16.
 */
public class Guest {
    @NotBlank(message = "{guest.name}")
    private String name;

    private Integer sex;

    private String accessTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex == null?0:sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAccessTime() {
        return accessTime == null? DateUtil.getCurrentTimeStr():accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", accessTime='" + accessTime + '\'' +
                '}';
    }
}
