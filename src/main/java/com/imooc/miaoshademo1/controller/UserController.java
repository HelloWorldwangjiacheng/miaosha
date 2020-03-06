package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.result.Result;
import com.imooc.miaoshademo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author w1586
 * @Date 2020/3/6 14:34
 * @Cersion 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<User> info(Model model,
                             @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                             @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                             HttpServletResponse response)
    {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        return Result.success(user);
    }
}
