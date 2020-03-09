package com.imooc.miaoshademo1.service;

import com.imooc.miaoshademo1.domain.Goods;
import com.imooc.miaoshademo1.domain.MiaoshaOrder;
import com.imooc.miaoshademo1.domain.OrderInfo;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.MiaoshaKey;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.util.MD5Util;
import com.imooc.miaoshademo1.util.UUIDUtil;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

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
     * 数字验证码的符号
     */
    private static char[] ops = new char[] {'+', '-', '*'};

    /**
     * 秒杀操作
     *
     * @param user
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo miaosha(User user, GoodsVo goodsVo) {
        // 减库存 下订单 写入秒杀订单
        //1.减库存,因为减库存有可能会失败，所以用Boolean类型来判断
        boolean reduceSuccess = goodsService.reduceStock(goodsVo);
        if (reduceSuccess) {
            //2.之前成功减库存，下订单
            // 3.写入秒杀订单（2、3都是和Order有关，所以都放在OrderService中实现）
            return orderService.createOrder(user, goodsVo);
        } else {
            setGoodsOver(goodsVo.getId());
            return null;
        }


    }


    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getOrderMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
        if (order != null) {
            //秒杀成功
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }

    /**
     * 检查该路径合法性
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(User user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }

    /**
     * 生成秒杀接口路径
     *
     * @param user
     * @param goodsId
     * @return
     */
    public String createMiaoshaPath(User user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }

    public BufferedImage createVerifyCode(User user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    /**
     *
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCode(User user, Long goodsId, Integer verifyCode) {
        if(user == null || goodsId <=0 || verifyCode == null) {
            return false;
        }
        Integer codeOld =
                redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return true;
    }

    /**
     * 计算
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * + - *  生成验证码
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }
}
