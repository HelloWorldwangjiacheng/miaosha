package com.imooc.miaoshademo1.service.impl;

import com.imooc.miaoshademo1.dao.UserDao;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.exception.GlobalException;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.redis.UserKey;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.service.UserService;
import com.imooc.miaoshademo1.util.MD5Util;
import com.imooc.miaoshademo1.util.UUIDUtil;
import com.imooc.miaoshademo1.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author w1586
 * @Date 2020/3/3 0:18
 * @Cersion 1.0
 */
@Service
public class UserServiceImpl implements UserService {

//    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private UserDao userDao;
    @Autowired
    RedisService redisService;

    @Override
    public User getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null){
//            return CodeMsg.SERVER_ERROR;
            throw  new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        // 判断手机号是否存在
        Long num = Long.parseLong(mobile);
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

        // 生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
//        return CodeMsg.SUCCESS;
    }

    @Override
    public User getByToken(HttpServletResponse response, String token) {
        //参数验证
        if (StringUtils.isEmpty(token)){
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        // 延长有效期
        if (user!=null){
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     * 添加cookie
     * @param response
     * @param token
     * @param user
     */
    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
