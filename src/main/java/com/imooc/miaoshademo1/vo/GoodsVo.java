package com.imooc.miaoshademo1.vo;

import com.imooc.miaoshademo1.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 * @author w1586
 */
@Data
public class GoodsVo extends Goods {
	private Double miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;

}
