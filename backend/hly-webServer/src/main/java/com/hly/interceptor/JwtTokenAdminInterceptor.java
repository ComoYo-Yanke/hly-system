package com.hly.interceptor;


import com.hly.constant.ErrorCodeConstant;
import com.hly.properties.JwtProperties;
import com.hly.utils.JwtUtil;
import com.hly.utils.ThreadLocalUtil;
import com.hly.utils.TokenCheckUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.rmi.server.ExportException;


/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private TokenCheckUtil tokenCheckUtil;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader("token");
        if(token == null || token.isEmpty()){
            log.info("令牌为空, 响应401000");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(ErrorCodeConstant.TOKEN_IS_NULL);
            return false;
        }
        
        try{
            JwtUtil.parseJWT(token);
        }
        catch (ExpiredJwtException ex){
            log.info("令牌过期, 响应401000");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(ErrorCodeConstant.TOKEN_EXPIRED);
            return false;
        }
        catch (Exception e){
            log.info("令牌非法, 响应401000");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(ErrorCodeConstant.TOKEN_IS_FALSE);
            return false;
        }
        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(token);
            Integer userId = JwtUtil.getUserIdByToken(token);
            log.info("当前用户id：", userId);
            ThreadLocalUtil.setCurrentIdS(userId);
            
            // 进行后登踢先登操作
            // TODO 后续换成websocket
            if(tokenCheckUtil.tokenIsRepeated(token)){
                log.info("有重复登录");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(ErrorCodeConstant.IS_REPEAT_LOGIN);
                return false;
            }
            if(!tokenCheckUtil.tokenIsExist(userId, token)){
                log.info("用户并未登录！");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(ErrorCodeConstant.NO_LOGIN_FINALLY);
                return false;
            }
            
            //3、通过，放行
            log.info("令牌合法，放行");
            return true;
        } catch (ExpiredJwtException ex){
            log.info("令牌过期, 响应401000");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(ErrorCodeConstant.TOKEN_EXPIRED);
            return false;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            log.info("令牌非法, 响应401000");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(ErrorCodeConstant.TOKEN_IS_FALSE);
            return false;
        }
    }
    
    @Override
    public void afterCompletion(jakarta.servlet.http.HttpServletRequest request,
                                jakarta.servlet.http.HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        ThreadLocalUtil.clear();
    }
    
    
}
