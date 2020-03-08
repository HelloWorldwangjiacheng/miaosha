package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.rabbitmq.MQSender;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.redis.UserKey;
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

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    @GetMapping("/db/getById")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getById(1L);
        return Result.success(user);
    }

//    @GetMapping("/redis/get")
//    @ResponseBody
//    public Result<User> redisGet(){
//        User user = redisService.get(UserKey.getById, ""+1, User.class);
//        return Result.success(user);
//    }
//
//    @GetMapping("/redis/set")
//    @ResponseBody
//    public Result<Boolean> redisSet(){
//        User user = new User();
//        user.setId(1L);
//        user.setNickname("1111");
//        boolean set = redisService.set(UserKey.getById, "" + 1, user);
//        return Result.success(set);
//    }

    @ResponseBody
    @RequestMapping("/mq")
    public Result<String> mq(){
        mqSender.send("hello wangjiacheng");
        return Result.success("hello world");
    }

    @ResponseBody
    @RequestMapping("/mq/topic")
    public Result<String> mqTopic(){
        mqSender.sendTopic("hello wangjiacheng");
        return Result.success("hello world");
    }

    @ResponseBody
    @RequestMapping("/mq/fanout")
    public Result<String> mqFanout(){
        mqSender.sendFanout("hello wangjiacheng");
        return Result.success("hello world");
    }

    @ResponseBody
    @RequestMapping("/mq/header")
    public Result<String> mqHeader(){
        mqSender.sendHeader("hello wangjiacheng");
        return Result.success("hello world");
    }
}
