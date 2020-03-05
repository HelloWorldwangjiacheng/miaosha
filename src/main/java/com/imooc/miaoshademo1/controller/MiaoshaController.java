package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.domain.MiaoshaOrder;
import com.imooc.miaoshademo1.domain.OrderInfo;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.service.GoodsService;
import com.imooc.miaoshademo1.service.MiaoshaService;
import com.imooc.miaoshademo1.service.OrderService;
import com.imooc.miaoshademo1.service.UserService;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author w1586
 * @Date 2020/3/5 22:18
 * @Cersion 1.0
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @PostMapping("/do_miaosha")
    public String list(Model model,
                       @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                       @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                       HttpServletResponse response,
                       @RequestParam("goodsId") Long goodsId)
    {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);

        model.addAttribute("user", user);

        if (user == null){
            return "login";
        }

        //判断商品库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stockCount = goodsVo.getStockCount();
        if (stockCount<=0){
            model.addAttribute("errMsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        //判断是否秒杀到了
        MiaoshaOrder order =
                orderService.getOrderMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order!=null){
            model.addAttribute("errMsg", CodeMsg.REPEAT_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        //减库存 下订单 写入秒杀订单 在事务里面做
        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goodsVo);
        return "order_detail";
    }
}
