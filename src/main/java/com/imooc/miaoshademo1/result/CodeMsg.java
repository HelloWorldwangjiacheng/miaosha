package com.imooc.miaoshademo1.result;

import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.Data;

/**
 * @Author w1586
 * @Date 2020/3/2 21:00
 * @Cersion 1.0
 */
@Data
public class CodeMsg {

    private int code;
    private String msg;

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 通用异常，
     */
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");

    /**
     * 登录模块 5002XX
     */


    /**
     * 商品模块 5003XX
     */


    /**
     * 订单模块，5004XX
     */


    /**
     * 秒杀模块，5005XX
     */


}
