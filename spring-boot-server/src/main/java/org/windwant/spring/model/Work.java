package org.windwant.spring.model;

import java.io.Serializable;

/**
 * Created by Administrator on 18-5-17.
 */
public class Work implements Serializable {
    private Integer id;

    private Integer sub_id;

    private String name;

    private byte[] content;

    public Integer getSub_id() {
        return sub_id;
    }

    public void setSub_id(Integer sub_id) {
        this.sub_id = sub_id;
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
