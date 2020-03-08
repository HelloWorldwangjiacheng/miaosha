package com.imooc.miaoshademo1.rabbitmq;


import com.imooc.miaoshademo1.domain.User;
import lombok.Data;

/**
 * @author w1586
 */
@Data
public class MiaoshaMessage {
	private User user;
	private Long goodsId;
}
