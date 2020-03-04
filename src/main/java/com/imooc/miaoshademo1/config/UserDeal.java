package com.imooc.miaoshademo1.config;

import java.lang.annotation.*;

/**
 * @Author w1586
 * @Date 2020/3/4 15:54
 * @Cersion 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserDeal {
    boolean required() default true;
}
