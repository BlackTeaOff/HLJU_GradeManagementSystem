package com.example.grademanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 拦截器, 需要验证Token(权限)的功能会被拦截
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        registry.addInterceptor(loginInterceptor)
                // 拦截所有 api 下的请求(因为都需要验证权限, 用Token里的ID验证)
                .addPathPatterns("/api/**")
                // 放行无需登录的接口, 登录、注册用户接口(因为还没有Token, Token登录完才发放)
                .excludePathPatterns(
                        "/api/v1/users/login",
                        "/api/v1/users/student",
                        "/api/v1/users/teacher",
                        "/api/v1/users/admin"
                );
    }
}
