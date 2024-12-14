package com.xiaofutest.Config.token;

import cn.hutool.core.text.CharSequenceUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.xiaofutest.model.LoginDTO;
import com.xiaofutest.repository.LoginDB;
import com.xiaofutest.unit.BusinessException;
import com.xiaofutest.unit.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * @author young
 * @date 2022/9/12 15:37
 * @description: 获取token并验证
 */
@Component
public class MyJwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否通过有PassToken注解
        if (method.isAnnotationPresent(PassToken.class)) {
            //如果有则跳过认证检查
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //否则进行token检查
        if (CharSequenceUtil.isBlank(token)) {
            throw new BusinessException(ErrorCode.TOKEN_EX, ErrorCode.TOKEN_EX.getMessage());
        }
        //获取token中的用户id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new BusinessException(ErrorCode.TOKEN_EX, ErrorCode.TOKEN_EX.getMessage());
        }

        //根据token中的userId查询数据库
        LoginDTO user = LoginDB.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_EX, ErrorCode.USER_EX.getMessage());
        }

        //验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new BusinessException(ErrorCode.VERIFICATION_FAILED, ErrorCode.VERIFICATION_FAILED.getMessage());
        }
        return true;
    }
}