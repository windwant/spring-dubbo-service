package org.windwant.spring.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.windwant.spring.model.Response;
import org.windwant.spring.util.LangUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/16.
 */
public class BaseController {
    public static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    LangUtil langUtil;

    protected Map<String, Object> validateBindingResult(String method, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getSimpleName());
            sb.append(" ");
            sb.append(method);
            sb.append(" ");
            sb.append("param validate failed: ");
            sb.append("\n");
            sb.append(result.getTarget());
            sb.append(" ");
            for (ObjectError error : errors) {
                sb.append(error.getDefaultMessage());
            }
            logger.warn("{}", sb.toString());
            return Response.response(-1, errors.get(0).getDefaultMessage());
        }else {
            return null;
        }
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<?> errorJson(HttpServletRequest req, Exception e) throws Exception {
        logger.error("exception occurs in rest controller: {}, {}, {}" , new Object[]{getClass().getSimpleName(), req.getServletPath(), e.getMessage()});
        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");

        switch (e.getClass().getSimpleName()){
            case "UnauthorizedException":
                return new ResponseEntity<>(Response.response(Response.CODE_403, Response.MSG_ERROR_FORBIDDEN), HttpStatus.valueOf(Response.CODE_403));
            case "MissingServletRequestParameterException": //required parameter
                return new ResponseEntity<>(Response.response(Response.CODE_403, e.getMessage()), HttpStatus.valueOf(Response.CODE_403));
            case "BindException": //bind exception
                return new ResponseEntity<>(Response.response(Response.CODE_403, ((BindException) e).getFieldError().getDefaultMessage()), HttpStatus.valueOf(Response.CODE_403));
        }

        if (statusCode == null) {
            return new ResponseEntity<>(Response.response(Response.CODE_500, Response.MSG_ERROR_EXCEPTION), HttpStatus.valueOf(Response.CODE_500));
        } else {
            return new ResponseEntity<>(Response.response(statusCode, e.getMessage()), HttpStatus.valueOf(statusCode));
        }
    }
}
