package com.imooc.miaoshademo1.controller;

import com.imooc.miaoshademo1.domain.OrderInfo;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.exception.GlobalException;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.result.Result;
import com.imooc.miaoshademo1.service.GoodsService;
import com.imooc.miaoshademo1.service.OrderService;
import com.imooc.miaoshademo1.service.UserService;
import com.imooc.miaoshademo1.vo.GoodsVo;
import com.imooc.miaoshademo1.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author w1586
 */
@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model,
									  @RequestParam("orderId") Long orderId,
									  @CookieValue(value = UserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
									  @RequestParam(value = UserService.COOKIE_NAME_TOKEN, required = false) String paramToken,
									  HttpServletResponse response,
									  HttpServletRequest request)
	{
		if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			throw new GlobalException(CodeMsg.SESSION_ERROR);
		}
		String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
		User user = userService.getByToken(response, token);
		model.addAttribute("user", user);
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return Result.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Result.success(vo);
    }
    
}
