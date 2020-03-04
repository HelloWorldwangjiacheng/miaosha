package com.imooc.miaoshademo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author w1586
 */
@SpringBootApplication
@ComponentScan(basePackages={"com.imooc.miaoshademo1.*"})
public class MiaoshaDemo1Application {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaDemo1Application.class, args);
    }

}
