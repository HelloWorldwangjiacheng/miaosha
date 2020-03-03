package com.imooc.miaoshademo1.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author w1586
 * @Date 2020/3/3 22:53
 * @Cersion 1.0
 */
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if (StringUtils.isEmpty(src)){
            return false;
        }

        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }


}
