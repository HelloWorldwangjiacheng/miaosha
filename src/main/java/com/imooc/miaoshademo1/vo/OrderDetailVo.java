package com.imooc.miaoshademo1.vo;

import com.imooc.miaoshademo1.domain.OrderInfo;
import lombok.Data;

/**
 * @author w1586
 */
@Data
public class OrderDetailVo {
	private GoodsVo goods;
	private OrderInfo order;
}
