package com.imooc.miaoshademo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author w1586
 * 如果要打成war包需要在MiaoshaDemo1Application 后面添加上extends SpringBootServletInitializer
 * 以及重写某些方法
 */
@SpringBootApplication
@ComponentScan(basePackages={"com.imooc.miaoshademo1.*"})
public class MiaoshaDemo1Application {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaDemo1Application.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
////        return super.configure(builder);
//        return builder.sources(MiaoshaDemo1Application.class);
//    }
}

