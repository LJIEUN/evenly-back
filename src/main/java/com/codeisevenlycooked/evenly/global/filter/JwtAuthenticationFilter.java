package com.codeisevenlycooked.evenly.global.filter;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            //JWT 가져오기
            String token = jwtUtil.resolveToken(request);

            //JWT 검증 후 SecurityContext에 저장
            if (token != null && jwtUtil.validateAccessToken(token)) {
                saveAuthentication(token);
            }
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Invalid Token\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    //SecurityContext에 userId 저장
    private void saveAuthentication(String token) {
        Claims claims = jwtUtil.parseClaims(token);
        String userId = claims.getSubject();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, null);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
