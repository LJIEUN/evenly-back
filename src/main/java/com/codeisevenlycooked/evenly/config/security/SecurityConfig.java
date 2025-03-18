package com.codeisevenlycooked.evenly.config.security;

import com.codeisevenlycooked.evenly.global.filter.JwtAuthenticationFilter;
import com.codeisevenlycooked.evenly.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // JWT 사용으로 필요 없음
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(request -> request
                    .requestMatchers("/auth/login", "/auth/signup").permitAll() // 로그인, 회원가입 인증 없이
                    .requestMatchers("/", "/products/**").permitAll()// 홈화면(상품 목록), 상품 상세, 등 추가 필요
                    .anyRequest().authenticated() // 그외 모든 요청은 인증 필요
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
