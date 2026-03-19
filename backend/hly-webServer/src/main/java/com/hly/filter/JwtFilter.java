//package com.hly.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hly.constant.InfoConstant;
//import com.hly.utils.JwtUtil;
//import com.hly.utils.ThreadLocalUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Slf4j
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//
//    private final JwtUtil jwtUtil;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Autowired
//    public JwtFilter(JwtUtil jwtUtil){
//        this.jwtUtil = jwtUtil;
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain chain) throws ServletException, IOException {
//
//        try {
//            // 1. 从请求头获取token
//            String token = getTokenFromRequest(request);
//            if (token == null) {
//                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), InfoConstant.RESPONSE_401000);
//                chain.doFilter(request, response);
//                return;
//            }
//
//            // 验证token
//            if(!jwtUtil.validate(token)){
//                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), InfoConstant.RESPONSE_401000);
//                chain.doFilter(request, response);
//                return;
//            }
//            // 解析userId
//            Integer userId = jwtUtil.getUserId(token);
//            if (jwtUtil.getUserId(token) == null) {chain.doFilter(request, response);}
//
//            // 存入userId
//            ThreadLocalUtil.setCurrentIdS(userId);
//
//        } catch (Exception e) {
//            // 捕获异常，不中断过滤器链，仅打印日志
//            log.error("认证失败：", e);
//            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), InfoConstant.RESPONSE_401000);
//            chain.doFilter(request, response);
//        }
//
//
//    }
//
//    /**
//     * 从请求头提取token
//     */
//    private String getTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    private void sendErrorResponse(HttpServletResponse response, int statusCode, String msg) throws IOException {
//        // 1. 设置响应头（JSON+UTF8+跨域）
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(statusCode);
//
//        // 跨域头（必须加，否则前端拿不到401响应）
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
//
//        // 2. 构建和业务一致的错误响应体
//        Map<String, Object> errorMap = new HashMap<>();
//        errorMap.put("code", statusCode);
//        errorMap.put("msg", msg);
//        errorMap.put("data", null);
//
//        // 3. 写入响应
//        response.getWriter().write(objectMapper.writeValueAsString(errorMap));
//    }
//}