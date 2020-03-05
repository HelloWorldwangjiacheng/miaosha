package com.imooc.miaoshademo1.service;

import com.imooc.miaoshademo1.domain.Goods;
import com.imooc.miaoshademo1.domain.OrderInfo;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author w1586
 * @Date 2020/3/5 22:34
 * @Cersion 1.0
 */
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(User user, GoodsVo goodsVo){
        // 减库存 下订单 写入秒杀订单
        //1.减库存
        goodsService.reduceStock(goodsVo);

        //2.下订单
        // 3.写入秒杀订单（2、3都是和Order有关，所以都放在OrderService中实现）
        return orderService.createOrder(user, goodsVo);

    }


}
