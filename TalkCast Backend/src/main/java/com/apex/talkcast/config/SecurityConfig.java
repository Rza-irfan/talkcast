package com.apex.talkcast.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(AnyRequestMatcher.INSTANCE).permitAll()
//                )
//                .cors(Customizer.withDefaults()) // ✅ Modern way
//                .build();
//    }
}
