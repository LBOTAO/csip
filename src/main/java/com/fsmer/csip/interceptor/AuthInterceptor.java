package com.fsmer.csip.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsmer.csip.annotation.IgnoreSecurity;
import com.fsmer.csip.entity.AuthUser;
import com.fsmer.csip.entity.response.ResponseWrapper;
import com.fsmer.csip.enumeration.ResponseCode;
import com.fsmer.csip.repository.AuthUserRepository;
import com.fsmer.csip.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private AuthUserRepository authUserRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(IgnoreSecurity.class)) {
            return true;
        }
        logger.info(request.getRequestURI());
        if (request.getRequestURI().contains("/login")|| request.getRequestURI().contains("/ws") ||request.getRequestURI().contains("/callback") || request.getRequestURI().contains("/error")){
            return  true;
        }
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) token = request.getParameter("access_token");
        if (StringUtils.isEmpty(token)) {
           responseTokenNotValid(response);
           return false;
        }
        String userId = JwtUtil.verify(token);
        if (StringUtils.isEmpty(userId)) {
            responseTokenNotValid(response);
            return false;
        }
        Optional<AuthUser> optionalUser = authUserRepository.findById(userId);
        if (!optionalUser.isPresent()) {

            responseTokenNotValid(response);
            return false;
        }
        request.setAttribute("currentUser", optionalUser.get());
        return true;
    }

    public void responseTokenNotValid(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .write(
                        new ObjectMapper()
                                .writeValueAsString(
                                        ResponseWrapper
                                                .createByErrorCodeMessage(
                                                        ResponseCode.TOKEN_NOT_VALID.getCode(),
                                                        ResponseCode.TOKEN_NOT_VALID.getMsg()
                                                )
                                )
                );
    }
}
