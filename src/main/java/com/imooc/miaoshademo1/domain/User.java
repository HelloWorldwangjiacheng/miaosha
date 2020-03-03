package com.imooc.miaoshademo1.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author w1586
 * @Date 2020/3/3 0:14
 * @Cersion 1.0
 */
@Data
public class User {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
