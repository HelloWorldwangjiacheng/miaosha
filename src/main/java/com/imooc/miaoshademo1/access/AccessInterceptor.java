package com.imooc.miaoshademo1.access;

import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.redis.AccessKey;
import com.imooc.miaoshademo1.redis.RedisService;
import com.imooc.miaoshademo1.result.CodeMsg;
import com.imooc.miaoshademo1.result.Result;
import com.imooc.miaoshademo1.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

/**
 * @author w1586
 */
@Service
public class AccessInterceptor  extends HandlerInterceptorAdapter{
	
	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;

    /**
     * 在方法进入之前做一些拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response,
							 Object handler)
			throws Exception {
		if(handler instanceof HandlerMethod) {
			User user = getUser(request, response);
			UserContext.setUser(user);
			HandlerMethod hm = (HandlerMethod)handler;
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if(accessLimit == null) {
				return true;
			}

			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();

			if(needLogin) {
				if(user == null) {
					render(response, CodeMsg.SESSION_ERROR);
					return false;
				}
				key += "_" + user.getId();
			}else {
				//do nothing
			}
			AccessKey ak = AccessKey.withExpire(seconds);
			Integer count = redisService.get(ak, key, Integer.class);
	    	if(count  == null) {
	    		 redisService.set(ak, key, 1);
	    	}else if(count < maxCount) {
	    		 redisService.incr(ak, key);
	    	}else {
	    		render(response, CodeMsg.ACCESS_LIMIT_REACHED);
	    		return false;
	    	}
		}
		return true;
	}
	
	private void render(HttpServletResponse response, CodeMsg cm)
            throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str  = JSON.toJSONString(Result.error(cm));
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}

	private User getUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
		String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(response, token);
	}
	
	private String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null || cookies.length <= 0){
			return null;
		}
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookieName)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
}
