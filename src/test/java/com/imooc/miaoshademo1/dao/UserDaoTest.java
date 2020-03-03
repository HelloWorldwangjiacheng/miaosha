package com.imooc.miaoshademo1.dao;

import com.imooc.miaoshademo1.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.Assert.*;
/**
 * @Author w1586
 * @Date 2020/3/3 23:56
 * @Cersion 1.0
 */
@SpringBootTest
class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    void getById() {
        Long num = Long.parseLong("13123868930");
        User user = userDao.getById(num);
        Assert.isNull(user);
        System.out.println(user.getNickname());
    }

    @Test
    void getByName() {

        User user = userDao.getByName("wjc");
//        Assert.isNull(user);
        System.out.println(user.getNickname());
    }
}