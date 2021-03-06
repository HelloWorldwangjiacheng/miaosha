package com.imooc.miaoshademo1.rabbitmq;

import com.imooc.miaoshademo1.domain.MiaoshaOrder;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.service.GoodsService;
import com.imooc.miaoshademo1.service.MiaoshaService;
import com.imooc.miaoshademo1.service.OrderService;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author w1586
 * @Date 2020/3/8 14:52
 * @Cersion 1.0
 */
@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;


    @RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
    public void receive1(String message) {
        log.info("receive message:"+message);
        MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
        User user = mm.getUser();
        Long goodsId = mm.getGoodsId();

        //这个是从数据库中拿的
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getOrderMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null) {
            //已经秒杀到了，那就什么也不做
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }

//		@RabbitListener(queues=MQConfig.QUEUE)
//		public void receive(String message) {
//			log.info("receive message:"+message);
//		}
//
//		@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
//		public void receiveTopic1(String message) {
//			log.info(" topic  queue1 message:"+message);
//		}
//
//		@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
//		public void receiveTopic2(String message) {
//			log.info(" topic  queue2 message:"+message);
//		}
//
//		@RabbitListener(queues=MQConfig.HEADER_QUEUE)
//		public void receiveHeaderQueue(byte[] message) {
//			log.info(" header  queue message:"+new String(message));
//		}
}
