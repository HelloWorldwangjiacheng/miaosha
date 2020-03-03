package com.imooc.miaoshademo1.redis;

import lombok.Data;

/**
 * @Author w1586
 * @Date 2020/3/3 19:39
 * @Cersion 1.0
 */
public class UserKey extends BasePrefix{

    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
