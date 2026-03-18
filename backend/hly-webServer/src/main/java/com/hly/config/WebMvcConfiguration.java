package com.hly.config;

import com.hly.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置类
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        log.info("注册自定义拦截器");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/user/login", "/api/user/register");
                
    }
    
    
}
