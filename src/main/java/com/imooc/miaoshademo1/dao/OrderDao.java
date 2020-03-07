package com.imooc.miaoshademo1.dao;

        import com.imooc.miaoshademo1.domain.MiaoshaOrder;
        import com.imooc.miaoshademo1.domain.OrderInfo;
        import org.apache.ibatis.annotations.*;

/**
 * @Author w1586
 * @Date 2020/3/5 22:40
 * @Cersion 1.0
 */
@Mapper
public interface OrderDao {

        /**
         * 通过用户id和产品id得到秒杀订单
         * @param userId
         * @param goodsId
         * @return
         */
        @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
        public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

        /**
         * 插入订单详情
         * @param orderInfo
         * @return
         */
        @Insert("insert into order_info" +
                "(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date) " +
                "values" +
                "(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, " +
                "#{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
        @SelectKey(keyColumn="id", keyProperty="id", resultType=Long.class, before=false,
                statement="select last_insert_id()")
        public Long insert(OrderInfo orderInfo);

        /**
         * 插入秒杀订单详情
         * @param miaoshaOrder
         * @return
         */
        @Insert("insert into miaosha_order (user_id, goods_id, order_id)" +
                "values(#{userId}, #{goodsId}, #{orderId})")
        public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

        /**
         * 通过订单id查找规定的订单
         * @param orderId
         * @return
         */
        @Select("select * from order_info where id = #{orderId}")
        public OrderInfo getOrderById(@Param("orderId") Long orderId);
}
