package com.imooc.miaoshademo1.config;

import com.imooc.miaoshademo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @Author w1586
 * @Date 2020/3/4 15:58
 * @Cersion 1.0
 */
@Service
public class UserDealArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(UserDeal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory)
            throws Exception {
        Annotation[] annotations = parameter.getParameterAnnotations();
        // 逐一处理
        for (Annotation annotation : annotations) {
            // userId
            if (annotation instanceof UserDeal) {
                return resolveUserDeal((UserDeal) annotation,
                        parameter,
                        modelAndViewContainer,
                        nativeWebRequest,
                        webDataBinderFactory);
            }
        }
        return null;
    }

    private Object resolveUserDeal(UserDeal annotation,
                                   MethodParameter parameter,
                                   ModelAndViewContainer modelAndViewContainer,
                                   NativeWebRequest nativeWebRequest,
                                   WebDataBinderFactory webDataBinderFactory) {


        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);

        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }

        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;

        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
