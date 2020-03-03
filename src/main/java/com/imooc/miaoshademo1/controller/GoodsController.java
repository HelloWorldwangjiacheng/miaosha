package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author w1586
 * @Date 2020/3/4 2:49
 * @Cersion 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;


    @GetMapping("/to_list")
    public String toList(Model model,
                         @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                         @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken
    ){
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        User user = userService.getByToken(token);


        model.addAttribute("user", user);


        return "goods_list";
    }



}
