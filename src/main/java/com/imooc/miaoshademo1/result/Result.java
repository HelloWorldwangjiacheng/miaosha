package com.imooc.miaoshademo1.result;

import lombok.Data;

/**
 * @Author w1586
 * @Date 2020/3/2 20:46
 * @Cersion 1.0
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(T data){
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    public Result(){}

    private Result(CodeMsg codeMsg) {
        if (codeMsg == null) {
            return ;
        }
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }


    /**
     * 成功时候的调用
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<>(data);
    }

    /**
     * 失败时候的调用
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }


}
