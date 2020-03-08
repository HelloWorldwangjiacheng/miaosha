package com.imooc.miaoshademo1.service;

import com.imooc.miaoshademo1.domain.Goods;
import com.imooc.miaoshademo1.domain.MiaoshaOrder;
import com.imooc.miaoshademo1.domain.OrderInfo;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.MiaoshaKey;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    RedisService redisService;

    /**
     * 秒杀操作
     * @param user
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo miaosha(User user, GoodsVo goodsVo){
        // 减库存 下订单 写入秒杀订单
        //1.减库存,因为减库存有可能会失败，所以用Boolean类型来判断
        boolean reduceSuccess = goodsService.reduceStock(goodsVo);
        if (reduceSuccess){
            //2.之前成功减库存，下订单
            // 3.写入秒杀订单（2、3都是和Order有关，所以都放在OrderService中实现）
            return orderService.createOrder(user, goodsVo);
        }else {
            setGoodsOver(goodsVo.getId());
            return null;
        }


    }


    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getOrderMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
        if(order != null) {
            //秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }

}
