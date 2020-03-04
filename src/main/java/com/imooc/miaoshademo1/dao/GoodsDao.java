package com.imooc.miaoshademo1.dao;

import com.imooc.miaoshademo1.domain.MiaoshaGoods;
import com.imooc.miaoshademo1.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author w1586
 * @Date 2020/3/4 17:47
 * @Cersion 1.0
 */
@Mapper
public interface GoodsDao {

    /**
     * 返回所有商品
     * @return
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price " +
            "from miaosha_goods mg left join goods g " +
            "on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price " +
            "from miaosha_goods mg left join goods g " +
            "on mg.goods_id = g.id " +
            "where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

    @Update("update miaosha_goods " +
            "set stock_count = stock_count - 1 " +
            "where goods_id = #{goodsId}")
    public int reduceStock(MiaoshaGoods g);

}
