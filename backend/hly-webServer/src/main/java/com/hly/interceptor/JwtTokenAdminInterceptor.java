package com.hly.interceptor;


import com.hly.constant.ErrorCodeConstant;
import com.hly.properties.JwtProperties;
import com.hly.utils.JwtUtil;
import com.hly.utils.ThreadLocalUtil;
import com.hly.utils.TokenCheckUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader("token");
        ThreadLocalUtil.setCurrentIdS(JwtUtil.getUserIdByToken(token));
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
            Integer userId = JwtUtil.getUserIdByToken(token);
            log.info("当前用户id：", userId);
            ThreadLocalUtil.setCurrentIdS(userId);
            
            // 进行后登踢先登操作
            // TODO 后续换成websocket
            if(tokenCheckUtil.tokenIsRepeated(token)){
                log.info("有重复登录");
                response.setStatus(ErrorCodeConstant.TOKEN_DISABLE);
                return false;
            }
            if(!tokenCheckUtil.tokenIsExist(userId, token)){
                log.info("用户并未登录！");
                return false;
            }
            
            //3、通过，放行
            log.info("令牌合法，放行");
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            log.info("令牌校验时出错了:{}",ex.getMessage());
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
