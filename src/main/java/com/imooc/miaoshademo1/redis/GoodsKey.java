package com.imooc.miaoshademo1.redis;

/**
 * @Author w1586
 * @Date 2020/3/7 14:39
 * @Cersion 1.0
 */
public class GoodsKey extends BasePrefix{
    private  GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    private GoodsKey(String prefix) {
        super(prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
}
