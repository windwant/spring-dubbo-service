package org.windwant.spring.model;

/**
 * Created by Administrator on 2018/1/16.
 */
public class User {
    private String userName;

    private String passwd;

    private Integer status;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static User build(String userName, String passwd, Integer status){
        User user = new User();
        user.setUserName(userName);
        user.setPasswd(passwd);
        user.setStatus(status);
        return user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
