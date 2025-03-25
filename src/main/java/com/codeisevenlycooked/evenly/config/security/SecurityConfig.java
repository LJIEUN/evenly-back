package com.codeisevenlycooked.evenly.config.security;

import com.codeisevenlycooked.evenly.global.filter.JwtAuthenticationFilter;
import com.codeisevenlycooked.evenly.service.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // JWT 사용으로 필요 없음
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(request -> request
                    .requestMatchers("/", "/css/**", "/products/**").permitAll()// 홈화면(상품 목록), 상품 상세, 등 추가 필요
                    .requestMatchers("/admin/**" ).permitAll()
                    .requestMatchers("/auth/login", "/auth/signup").permitAll() // 로그인, 회원가입 인증 없이
                    .anyRequest().authenticated() // 그외 모든 요청은 인증 필요
            )
            .exceptionHandling(e -> e
                    .authenticationEntryPoint((request, response, authException) -> {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("401 Unauthorized: 인증이 필요합니다.");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("403 Forbidden: 접근이 거부되었습니다.");
                    })

            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
