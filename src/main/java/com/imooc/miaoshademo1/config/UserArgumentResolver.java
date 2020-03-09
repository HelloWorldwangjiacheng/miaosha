package com.imooc.miaoshademo1.config;

import com.imooc.miaoshademo1.access.UserContext;
import com.imooc.miaoshademo1.domain.User;
import com.imooc.miaoshademo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author w1586
 * @Date 2020/3/4 3:37
 * @Cersion 1.0
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        return parameterType == User.class;
//        return methodParameter.hasParameterAnnotation(User.class)
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory)
            throws Exception {

        return UserContext.getUser();

//        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
//
//        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
//        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
//
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
//            return null;
//        }
//
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//
//        return userService.getByToken(response, token);
    }

//    private String getCookieValue(HttpServletRequest request, String cookieName) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null || cookies.length<=0){
//            return null;
//        }
//        for (Cookie cookie : cookies){
//            if (cookie.getName().equals(cookieName)){
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }

}
