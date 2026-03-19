//package com.hly.config;
//
//import com.hly.filter.JwtFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity // 启用Spring Security
//public class SecurityConfiguration {
//
//    @Autowired
//    private JwtFilter jwtFilter;
//
//    /**
//     * 配置安全过滤链
//     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // 关闭CSRF（前后端分离不需要）
//                .csrf(csrf -> csrf.disable())
//                // 关闭表单登录（用自定义JWT登录）
//                .formLogin(form -> form.disable())
//                // 关闭HTTP Basic认证
//                .httpBasic(basic -> basic.disable())
//                // 无状态会话（不创建session，JWT是无状态的）
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                // 权限配置
//                .authorizeHttpRequests(auth -> auth
//                        // 白名单：放行登录接口
//                        .requestMatchers("/api/user/login", "/api/user/register").permitAll()
//                        // 其他接口都需要认证
//                        .anyRequest().authenticated()
//                )
//                // 添加JWT过滤器（在UsernamePasswordAuthenticationFilter之前执行）
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//
//
//}
