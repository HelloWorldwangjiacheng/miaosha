package com.imooc.miaoshademo1.redis;

import lombok.Data;

/**
 * @Author w1586
 * @Date 2020/3/3 19:39
 * @Cersion 1.0
 */
public class UserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600*24*2;

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE,"tk");

}
