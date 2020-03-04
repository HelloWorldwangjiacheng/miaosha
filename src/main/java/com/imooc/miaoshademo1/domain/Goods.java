package com.imooc.miaoshademo1.domain;

import lombok.Data;

/**
 * @author w1586
 */
@Data
public class Goods {
	private Long id;
	private String goodsName;
	private String goodsTitle;
	private String goodsImg;
	private String goodsDetail;
	private Double goodsPrice;
	private Integer goodsStock;

}
