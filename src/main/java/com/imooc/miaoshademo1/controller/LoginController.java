package com.imooc.miaoshademo1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author w1586
 * @Date 2020/3/3 22:04
 * @Cersion 1.0
 */
@Controller
public class LoginController {

    @GetMapping("/toLogin")
    public String toLogin(){
        return "Login";
    }

    @GetMapping("/doLogin")
    @ResponseBody
    public String doLogin(){
        return null;
    }
}
