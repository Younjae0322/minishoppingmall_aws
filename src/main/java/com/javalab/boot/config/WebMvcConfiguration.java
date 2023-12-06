package com.javalab.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    public class WebMvcConfiguration implements WebMvcConfigurer {
        /*@Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 집
            // 로컬 버전
            registry.addResourceHandler("/files/**")
                    .addResourceLocations("file:///D:/utility/iworks/minishoppingmall/files/");
        }*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 학원
        // 로컬 버전
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///D:/iworks/jpa/minishoppingmall/files/");
    }

/*    // 클라우드 타입 버전
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///app/files/");
    }*/
}

