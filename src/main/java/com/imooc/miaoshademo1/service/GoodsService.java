package com.imooc.miaoshademo1.service;

import com.imooc.miaoshademo1.dao.GoodsDao;
import com.imooc.miaoshademo1.domain.MiaoshaGoods;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author w1586
 * @Date 2020/3/4 17:46
 * @Cersion 1.0
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;


    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int effectNum = goodsDao.reduceStock(g);
        return effectNum>0;
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }
}
