package com.imooc.miaoshademo1.service;

import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.vo.LoginVo;

/**
 * @Author w1586
 * @Date 2020/3/3 0:18
 * @Cersion 1.0
 */
public interface UserService {


    /**
     * 根据用户id返回User对象
     * @param id
     * @return
     */
    public User getById(Long id);

    public Boolean login(LoginVo loginVo);
}
