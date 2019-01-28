package org.windwant.spring.controller.rest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.windwant.common.api.model.Guest;
import org.windwant.common.api.model.Score;
import org.windwant.common.api.model.Student;
import org.windwant.common.api.model.User;
import org.windwant.dubbo.DubboSvr;
import org.windwant.spring.Constants;
import org.windwant.spring.core.spi.SpiService;
import org.windwant.spring.model.Response;
import org.windwant.spring.service.BootService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
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
        return DubboSvr.dubboRestService.hello(name);
    }

    @RequestMapping("/hellox")
    Map<String, Object> hellox(@Valid Guest guest, BindingResult result){
        if(result.hasErrors()){
            return Response.response(-1, Constants.FAILED, result.getAllErrors());
        }
        return Response.response(0, DubboSvr.dubboRestService.hellox(guest));
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
        return  bootService.login(user);
    }

    @Autowired
    private SpiService spiService;

    @RequestMapping("/spiCalc")
    Map<String, Object> spiCalc(Integer value){
        return Response.response(0, Response.MSG_SUCCESS, spiService.execCalc(value).toString());
    }

    @GetMapping("/mongo")
    Map<String, Object> testMongo(){
        DubboSvr.dubboRestService.testMongo();
        return Response.response(0, Response.MSG_SUCCESS);
    }

    /**
     * 一对一级联查询
     * @return
     */
    @GetMapping("/score/{id}")
    Map<String, Object> testAssociationScoreStu (@PathVariable int id,
                                                 @RequestParam(value = "s", required = false, defaultValue = "0") int source){
        Score score = DubboSvr.dubboRestService.getScoreById(id, source);
        logger.info("annotation mapper score: {}", ToStringBuilder.reflectionToString(score));
        return Response.response(0, Response.MSG_SUCCESS, score);
    }

    /**
     * 一对多级联查询
     * @return
     */
    @GetMapping("/stu/{id}")
    Map<String, Object> testAssociationStuScore (@PathVariable int id,
                                                 @RequestParam(value = "s", required = false, defaultValue = "0") int source,
                                                 int page){
        if(id == 0){
            List<Student> stus = DubboSvr.dubboRestService.getStu(page);
            logger.info("query stu list size: {}", stus.size());
            stus.stream().forEach(item -> logger.info("stu: {}", ToStringBuilder.reflectionToString(item)));
            return Response.response(0, Response.MSG_SUCCESS, stus);
        }
        Student stu = DubboSvr.dubboRestService.getStuById(id, source);
        logger.info("query stu: {}", ToStringBuilder.reflectionToString(stu));
        return Response.response(0, Response.MSG_SUCCESS, stu);
    }
}
