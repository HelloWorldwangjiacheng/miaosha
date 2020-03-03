package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.result.Result;
import com.imooc.miaoshademo1.service.UserService;
import com.imooc.miaoshademo1.util.ValidatorUtil;
import com.imooc.miaoshademo1.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author w1586
 * @Date 2020/3/3 22:04
 * @Cersion 1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/to_login")
    public String toLogin(){
        return "Login";
    }

    @PostMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo){
        logger.info(loginVo.toString());
        //参数校验
//        String passInput = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        if (StringUtils.isEmpty(passInput)){
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if (StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if (!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }

        // 登录操作
//        CodeMsg codeMsg = userService.login(loginVo);
//        if (codeMsg.getCode() == 0){
//            return Result.success(true);
//        }else {
//            return Result.error(codeMsg);
//        }

        userService.login(loginVo);
        return Result.success(true);

    }
}
