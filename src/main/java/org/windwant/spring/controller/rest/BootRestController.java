package org.windwant.spring.controller.rest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.windwant.spring.Constants;
import org.windwant.spring.core.spi.SpiService;
import org.windwant.spring.model.Guest;
import org.windwant.spring.model.Response;
import org.windwant.spring.model.User;
import org.windwant.spring.service.BootService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/15.
 */
@RestController
public class BootRestController extends BaseController {

    @Autowired
    private BootService bootService;

    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name){
        return bootService.hello(name);
    }

    @RequestMapping("/hellox")
    Map<String, Object> hellox(@Valid Guest guest, BindingResult result){
        if(result.hasErrors()){
            return Response.response(-1, Constants.FAILED, result.getAllErrors());
        }
        return Response.response(0, bootService.hellox(guest));
    }

    @RequestMapping(value= "/logout")
    public Map<String, Object>  logout() {
        SecurityUtils.getSubject().logout();
        return Response.response(0, Constants.SUCCESS);
    }

    @RequestMapping("/login/notlogin")
    public Map<String, Object> notLogin(HttpServletRequest request) {
        logger.info("{} not login!", request.getRequestURL().toString());
        return Response.response(401, langUtil.getMsg("login.not"));
    }


    @RequestMapping("/login")
    Map<String, Object> login(@Valid User user){
        return bootService.login(user);
    }

    @Autowired
    private SpiService spiService;

    @RequestMapping("/spiCalc")
    Map<String, Object> spiCalc(Integer value){
        return Response.response(0, Response.MSG_SUCCESS, spiService.execCalc(value).toString());
    }
}
