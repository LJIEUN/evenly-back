package com.codeisevenlycooked.evenly.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // POST 요청 허용
            .authorizeHttpRequests(request -> request
//                    .requestMatchers("auth/**").permitAll() // auth로 시작하는 url 인증 없이
                    // 여기에 홈화면(상품 목록), 상품 상세,
                    .anyRequest().permitAll() //개발 - 인증 없이 허용! // 그외 모든 요청은 인증 필요 .authenticated()
            )
                .formLogin(login -> login.disable()) // 기본 로그인 페이지 비활성화
                .httpBasic(basic -> basic.disable()); // 기본 인증 방식 비활성화
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
