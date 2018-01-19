package org.windwant.spring.core.mgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.windwant.spring.util.FTPTransport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by windwant on 2017/4/6.
 * JMX Mbean 监控 可以通过jconsole进行mbean暴露操作
 */
@Component
@ManagedResource(description = "sboot svr")
public class WAMBean {
    // 属性
    private String name;
    private int age;
    private String message;

    @ManagedAttribute
    public String getName() {
        System.out.println("name: " + name);
        return name;
    }

    @ManagedAttribute
    public void setName(String name) {
        this.name = name;
    }

    @ManagedAttribute
    public int getAge() {
        System.out.println("age: "+age);
        return age;
    }

    @ManagedAttribute
    public void setAge(int age) {
        this.age = age;
    }

    @ManagedAttribute
    public String getMessage() {
        System.out.println("message: " + message);
        return message;
    }

    @ManagedAttribute
    public void setMessage(String message) {
        this.message = message;
    }

    @ManagedOperation
    @ManagedOperationParameter(name = "message", description = "message")
    public void call(String message) {
        System.out.println("call：" + message);
    }

    @ManagedOperation
    @ManagedOperationParameter(name = "who", description = "who")
    @ManagedOperationParameter(name = "what", description = "what")
    public void look(String who, String what){
        System.out.println(who + " 发现了 " + what);
    }

    @Autowired
    FTPTransport ftpTransport;

    @ManagedOperation
    public void upload() throws FileNotFoundException {
        FileInputStream f = null;
        try {
            f = new FileInputStream(new File("D:\\a.json"));
            ftpTransport.uploadFile("ajson", f);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(f != null){
                    f.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        System.out.println("to play....");
    }

}
