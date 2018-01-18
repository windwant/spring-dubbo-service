package org.windwant.spring.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.windwant.spring.Constants;
import org.windwant.spring.controller.rest.BaseController;
import org.windwant.spring.model.Guest;
import org.windwant.spring.model.User;
import org.windwant.spring.util.ExcelUtil;
import org.windwant.spring.util.ImageUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */
@Controller
public class BootController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 验证码
     * @param request
     * @param response
     */
    @RequestMapping("/login/checkcode")
    public void getCheckCode(HttpServletRequest request, HttpServletResponse response) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String code = ImageUtil.drawImg(output);

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        session.setAttribute(Constants.SESSION_KEY_IMAGE, code);

        try {
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
        } catch (IOException e) {
            logger.warn("create the image code failed, error:", e);
        }
    }

    @RequestMapping("/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        List<User> users = new ArrayList(){{
            add(User.build("admin", "123", 1));
            add(User.build("guest", "123", 1));
        }};
        String[] exportFields = new String[]{"userName", "passwd"};
        String[] exportTitles = new String[]{"用户名", "密码"};
        String sheetName = "用户账号";
        String fileName = "账户资料";
        ExcelUtil.exportExcel(users, exportFields, exportTitles, sheetName, fileName, response);
    }
}
