package com.imooc.miaoshademo1.vo;

import lombok.Data;

/**
 * @Author w1586
 * @Date 2020/3/3 22:23
 * @Cersion 1.0
 */
@Data
public class LoginVo {
    private String mobile;
    private String password;

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
