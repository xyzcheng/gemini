package com.xyzc.gemini.controller;

import com.xyzc.gemini.login.service.LoginService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xyzc on 2016/9/27.
 */
@Controller
public class LoginController {

    private static Logger logger = Logger.getLogger(LoginController.class);
    @Resource
    LoginService loginService;
    @RequestMapping(value = "/addUser")
    public String addUser(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String password,
            HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return "regFail";
        }
        loginService.addUser(userName, password);
        return "regSucc";
    }

}
