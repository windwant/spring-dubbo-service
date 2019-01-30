package org.windwant.common.enums;

/**
 * Created by Administrator on 18-5-14.
 */
public enum Sex {
    MALE(0, "MALE"), FEMAIL(1, "FEMAIL");

    private int id;

    private String name;

    Sex() {}

    Sex(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Sex getSex(int id){
        return id == 0?MALE:FEMAIL;
    }
}
