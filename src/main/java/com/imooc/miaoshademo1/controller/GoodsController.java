package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.config.UserDeal;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.service.GoodsService;
import com.imooc.miaoshademo1.service.UserService;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author w1586
 * @Date 2020/3/4 2:49
 * @Cersion 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @GetMapping("/to_list")
    public String toList(Model model,
                         @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                         @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                         HttpServletResponse response
//                         @UserDeal User user
    ) {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);

        model.addAttribute("user", user);

        // 查询商品列表
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);


        return "goods_list";
    }

    @GetMapping("/to_detail/{goodsId}")
    public String toDetail(Model model,
                           @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                           @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                           HttpServletResponse response,
                           @PathVariable("goodsId") Long goodsId) {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        // 这里可用snowflake算法进行优化
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        // 秒杀状态
        int miaoshaStatus = 0;
        // 还有多少秒开始秒杀商品
        int remainSeconds = 0;

        if (now < startAt) {
            //秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (int)(startAt-now)/1000;
        } else if (now > endAt) {
            //秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goods_detail";
    }


}
