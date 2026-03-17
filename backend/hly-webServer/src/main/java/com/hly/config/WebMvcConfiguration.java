package com.hly.config;

import com.hly.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

/**
 * 配置类
 */
@Configuration
@Slf4j
public class WebMvcConfiguration {
    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    
    protected void addInterceptor(InterceptorRegistry registry){
        log.info("注册自定义拦截器");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/register")
                .addPathPatterns("/api/**");
    }
    
    
}
