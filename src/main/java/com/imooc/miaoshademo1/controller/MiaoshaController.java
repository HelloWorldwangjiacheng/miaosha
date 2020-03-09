package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.domain.MiaoshaOrder;
import com.imooc.miaoshademo1.domain.OrderInfo;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.rabbitmq.MQSender;
import com.imooc.miaoshademo1.rabbitmq.MiaoshaMessage;
import com.imooc.miaoshademo1.redis.GoodsKey;
import com.imooc.miaoshademo1.redis.MiaoshaKey;
import com.imooc.miaoshademo1.redis.OrderKey;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.result.Result;
import com.imooc.miaoshademo1.service.GoodsService;
import com.imooc.miaoshademo1.service.MiaoshaService;
import com.imooc.miaoshademo1.service.OrderService;
import com.imooc.miaoshademo1.service.UserService;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author w1586
 * @Date 2020/3/5 22:18
 * @Cersion 1.0
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
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

    @Autowired
    MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();


    /**
     * 系统初始化，也就是预加载
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null){
            return ;
        }
        //预加载商品数量到redis里面
        for (GoodsVo goodsVo : goodsVoList){
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goodsVo.getId(), goodsVo.getStockCount());
            //内存标记
            localOverMap.put(goodsVo.getId(), false);
        }

    }

    /**
     * 重置
     * @param model
     * @return
     */
    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsList);
        return Result.success(true);
    }

    /**
     * orderId: 成功
     * -1 ：秒杀失败
     *  0 ： 排队中
     * 库存不足
     * @param model
     * @param response
     * @param cookieToken
     * @param paramToken
     * @param goodsId
     * @return
     */
    @GetMapping(value="/result")
    @ResponseBody
    public Result<Long> getResult(Model model,
                                   HttpServletResponse response,
                                   @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                                   @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                                   @RequestParam("goodsId")Long goodsId) {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        model.addAttribute("user", user);

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        Long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);

        return Result.success(result);
    }

    /**
     * 没有优化前：
     * jmeter压迫测试 2000个用户 每个用户访问5次 一共100000次 QPS：217
     *
     * 优化之后：409
     *
     * GET 和 POST有什么区别？
     * GET幂等，向服务端拿数据   POST向服务端提交数据
     *
     * @param model
     * @param cookieToken
     * @param paramToken
     * @param response
     * @param goodsId
     * @return
     */
    @PostMapping(value="/{path}/do_miaosha")
    @ResponseBody
    public Result<Integer> miaosha(Model model,
                                   HttpServletResponse response,
                                   @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                                   @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                                   @RequestParam("goodsId")Long goodsId,
                                   @PathVariable("path") String path)
    {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        model.addAttribute("user", user);

        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        //内存标记，减少redis的访问
        Boolean over = localOverMap.get(goodsId);
        if (over){
            return  Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //预减库存
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0){
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断是否秒杀到商品了
        MiaoshaOrder order = orderService.getOrderMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order!=null){
            return Result.error(CodeMsg.REPEAT_MIAOSHA);
        }

        //入队
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setUser(user);
        mqSender.sendMiaoshaMessage(miaoshaMessage);
        // 0代表排队中
        return Result.success(0);

        /*
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        //10个商品，req1 req2
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getOrderMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null) {
            return Result.error(CodeMsg.REPEAT_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        //把订单返回出去
        return Result.success(orderInfo);

         */

    }

    /**
     * 得到秒杀接口的地址
     * @param request
     * @param response
     * @param cookieToken
     * @param paramToken
     * @param goodsId
     * @param verifyCode
     * @return
     */
//    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @GetMapping(value="/path")
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                                         @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                                         @RequestParam("goodsId") Long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode ,
                                         Model model

    ) {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        model.addAttribute("user",user);

        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path  =miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    /**
     * 验证码接口
     * @param response
     * @param request
     * @param cookieToken
     * @param paramToken
     * @param goodsId
     * @return
     */
    @GetMapping(value="/verifyCode")
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response,
                                              HttpServletRequest request,
                                              @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                                              @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
                                              @RequestParam("goodsId")long goodsId)
    {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);


        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }


    /**
     * 之前写的秒杀接口
     * @param model
     * @param cookieToken
     * @param paramToken
     * @param response
     * @param goodsId
     * @return
     */
    @PostMapping("/doMiaosha")
    public String doMiaosha(Model model,
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
        System.out.println("do_miaosha");
        return "order_detail";
    }

}
