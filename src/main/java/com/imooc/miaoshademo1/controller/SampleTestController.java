package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.result.Result;
import com.imooc.miaoshademo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author w1586
 * @Date 2020/3/2 22:15
 * @Cersion 1.0
 */
@Controller
@RequestMapping("/demo")
public class SampleTestController {

    @Autowired
    private UserService userService;

    @GetMapping("/db/getById")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }
}
