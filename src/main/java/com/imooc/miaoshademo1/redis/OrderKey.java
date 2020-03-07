package com.imooc.miaoshademo1.redis;

/**
 * @Author w1586
 * @Date 2020/3/3 19:40
 * @Cersion 1.0
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
