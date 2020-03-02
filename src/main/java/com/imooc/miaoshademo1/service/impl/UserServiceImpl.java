package com.imooc.miaoshademo1.service.impl;

import com.imooc.miaoshademo1.dao.UserDao;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author w1586
 * @Date 2020/3/3 0:18
 * @Cersion 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getById(int id) {

        return userDao.getById(id);
    }
}
