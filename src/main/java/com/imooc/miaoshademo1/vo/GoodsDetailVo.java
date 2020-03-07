package com.imooc.miaoshademo1.vo;

import com.imooc.miaoshademo1.domain.User;
import lombok.Data;

/**
 * @author w1586
 */
@Data
public class GoodsDetailVo {
	private int miaoshaStatus = 0;
	private int remainSeconds = 0;
	private GoodsVo goods ;
	private User user;
}
