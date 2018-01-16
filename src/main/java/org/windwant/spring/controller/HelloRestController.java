package org.windwant.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.windwant.spring.Constants;
import org.windwant.spring.model.Guest;
import org.windwant.spring.service.BootService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/15.
 */
@RestController
public class HelloRestController extends BaseController {

    @Autowired
    private BootService bootService;

    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name){
        return bootService.hello(name);
    }

    @RequestMapping("/hellox")
    Map<String, Object> hellox(@Valid Guest guest, BindingResult result){
        if(result.hasErrors()){
            return response(-1, Constants.FAILED, result.getAllErrors());
        }
        return response(0, bootService.hellox(guest));
    }
}
