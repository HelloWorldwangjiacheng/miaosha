package com.imooc.miaoshademo1.access;

import com.imooc.miaoshademo1.domain.User;
import lombok.Data;

/**
 * @author w1586
 */

public class UserContext {
	
	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

	public static void setUser(User user) {
		userHolder.set(user);
	}

	public static User getUser() {
		return userHolder.get();
	}
}
