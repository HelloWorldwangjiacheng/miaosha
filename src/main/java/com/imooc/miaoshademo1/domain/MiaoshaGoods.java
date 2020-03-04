package com.imooc.miaoshademo1.domain;

import lombok.Data;
import java.util.Date;

/**
 * @author w1586
 */

@Data
public class MiaoshaGoods {
	private Long id;
	private Long goodsId;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;

}
