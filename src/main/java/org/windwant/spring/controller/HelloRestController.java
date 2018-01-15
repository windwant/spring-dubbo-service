package org.windwant.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.windwant.spring.service.BootService;

/**
 * Created by Administrator on 2018/1/15.
 */
@RestController
public class HelloRestController {

    @Autowired
    private BootService bootService;

    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name){
        return bootService.hello(name);
    }

    @RequestMapping("/hellox/{name}")
    String hellox(@PathVariable String name){
        return bootService.hellox(name);
    }
}
