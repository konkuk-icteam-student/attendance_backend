package com.example.attendance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 적용
                .allowedOrigins("http://localhost:3000")  // 허용할 오리진 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메소드 지정
                .allowCredentials(true);
    }
}