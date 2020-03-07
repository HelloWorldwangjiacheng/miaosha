package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.GoodsKey;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.service.GoodsService;
import com.imooc.miaoshademo1.service.UserService;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 没有优化前 对/to_list 2000个线程 访问10次 一共20000次 吞吐量（QPS）是720
     * 页面缓存之后 同样条件 QPS是第一次960 第二次是2000
     * @param model
     * @param cookieToken
     * @param paramToken
     * @param response
     * @return
     */
    @GetMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(Model model,
                         @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                         @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                         HttpServletRequest request,
                         HttpServletResponse response
//                         @UserDeal User user
    ) {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);

        model.addAttribute("user", user);

        //页面缓存主要是为预防瞬间访问量，但其实页面缓存的时间不长只有60秒，也能做到一定程度的更新
        // 缓存时间太长，及时性就不是很好
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        // 查询商品列表
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
//        return "goods_list";

        //手动渲染
        WebContext webContext = new WebContext(
                request,
                response,
                request.getServletContext(),
                request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", webContext);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;

    }

    @GetMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail(Model model,
                           @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                           @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                           HttpServletResponse response,
                           HttpServletRequest request,
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
//        return "goods_detail";

        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // 手动渲染
        WebContext webContext = new WebContext(
                request,
                response,
                request.getServletContext(),
                request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", webContext);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }

}
