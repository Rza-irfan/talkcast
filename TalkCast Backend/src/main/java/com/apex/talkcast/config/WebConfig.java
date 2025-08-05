package com.apex.talkcast.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final YouTubeClientInterceptor youTubeClientInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Intercept only paths starting with /youtube-api/**
        registry.addInterceptor(youTubeClientInterceptor)
                .addPathPatterns("/api/youtube/**");
    }
}
