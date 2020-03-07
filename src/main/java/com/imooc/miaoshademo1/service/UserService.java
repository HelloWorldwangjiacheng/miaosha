package com.imooc.miaoshademo1.service;

import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author w1586
 * @Date 2020/3/3 0:18
 * @Cersion 1.0
 */
public interface UserService {
    public static final String COOKIE_NAME_TOKEN = "token";

    /**
     * 根据用户id返回User对象
     * @param id
     * @return
     */
    public User getById(Long id);

    /**
     * 处理登录过程
     * @param response
     * @param loginVo
     * @return
     */
    public String login(HttpServletResponse response, LoginVo loginVo);

    /**
     * 通过token来找到user
     * @param response
     * @param token
     * @return
     */
    User getByToken(HttpServletResponse response, String token);
}
