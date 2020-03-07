package com.imooc.miaoshademo1.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author w1586
 * @Date 2020/3/3 20:44
 * @Cersion 1.0
 */
public class MD5Util {

    private static final String MySalt = "1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToFormPass(String inputPass){
        String str = ""+MySalt.charAt(0) + MySalt.charAt(2) + inputPass + MySalt.charAt(5) + MySalt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt){
        String str = salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String input, String saltDB){
        String formPass = inputPassToFormPass(input);
//        System.out.println(formPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        System.out.println(dbPass);
        return dbPass;
    }

    public static void main(String[] args){
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
        //6e0a7fe692684372437c9e508508990d
    }
}
