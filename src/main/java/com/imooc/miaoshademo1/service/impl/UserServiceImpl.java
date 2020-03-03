package com.imooc.miaoshademo1.service.impl;

import com.imooc.miaoshademo1.dao.UserDao;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.exception.GlobalException;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.service.UserService;
import com.imooc.miaoshademo1.util.MD5Util;
import com.imooc.miaoshademo1.vo.LoginVo;
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
    public User getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public Boolean login(LoginVo loginVo) {
        if (loginVo == null){
//            return CodeMsg.SERVER_ERROR;
            throw  new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        // 判断手机号是否存在
        Long num = Long.parseLong(mobile);
//        System.out.println("+++++++______+++++++=>"+Long.class.isInstance(num));
        User user = userDao.getById(num);
        if (user == null){
//            return CodeMsg.MOBILE_NOT_EXIST;
            throw  new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        
        //验证密码
        String dbPass = user.getPassword();
        String dbSalt = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);

        if (!dbPass.equals(calcPass)){
//            return CodeMsg.PASSWORD_ERROR;
            throw  new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        return true;
//        return CodeMsg.SUCCESS;
    }
}
