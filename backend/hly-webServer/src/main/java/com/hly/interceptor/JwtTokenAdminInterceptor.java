package com.hly.interceptor;


import com.hly.constant.ErrorCodeConstant;
import com.hly.constant.InfoConstant;
import com.hly.constant.JwtClaimsConstant;
import com.hly.context.BaseContext;
import com.hly.properties.JwtProperties;
import com.hly.utils.JwtUtil;
import com.hly.utils.ThreadLocalUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader("token");
        if(token == null || token.isEmpty()){
            log.info("令牌为空, 响应401000");
            response.setStatus(ErrorCodeConstant.TOKEN_DISABLE);
            return false;
        }
        try{
            JwtUtil.parseJWT(token);
        }catch (Exception e){
            log.info("令牌非法, 响应401000");
            response.setStatus(ErrorCodeConstant.TOKEN_DISABLE);
            return false;
        }
        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(token);
            Integer userId = Integer.valueOf(claims.get(JwtClaimsConstant.ID).toString());
            log.info("当前用户id：", userId);
//            BaseContext.setCurrentId(userId);
            ThreadLocalUtil.setCurrentIdS(userId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(ErrorCodeConstant.TOKEN_DISABLE);
            return false;
        }
    }
    
    @Override
    public void afterCompletion(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        ThreadLocalUtil.clear();
    }
    
    
}
