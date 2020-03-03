package com.imooc.miaoshademo1.redis;

/**
 * @Author w1586
 * @Date 2020/3/3 19:17
 * @Cersion 1.0
 */
public interface KeyPrefix {

    /**
     * 过期
     * @return
     */
    public int expireSeconds();

    /**
     * 前缀
     * @return
     */
    public String getPrefix();

}
