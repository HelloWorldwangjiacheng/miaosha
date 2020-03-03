package com.imooc.miaoshademo1.exception;

import com.imooc.miaoshademo1.result.CodeMsg;
import lombok.Getter;

/**
 * @Author w1586
 * @Date 2020/3/4 1:03
 * @Cersion 1.0
 */
@Getter
public class GlobalException extends RuntimeException{

    private static final Long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

//    public CodeMsg getCm() {
//        return cm;
//    }

}


