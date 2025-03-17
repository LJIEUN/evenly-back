package com.codeisevenlycooked.evenly.config.security;

import com.codeisevenlycooked.evenly.dto.JwtUserInfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key accessKey;
    private final Key refreshKey;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    public JwtUtil(@Value("${jwt.secret}") String accessSecretKey,
                   @Value("${jwt.refresh_secret}") String refreshSecretKey,
                   @Value("${jwt.expiration_time}") long accessTokenExpireTime) {
        this.accessKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
        this.accessTokenExpireTime = accessTokenExpireTime * 1000L * 60;
        this.refreshTokenExpireTime = this.accessTokenExpireTime * 2;
    }

    //토큰 생성
    public String[] generateToken(JwtUserInfoDto user) {
        String accessToken = generateToken(user, accessKey, accessTokenExpireTime);
        String refreshToken = generateToken(user, refreshKey, refreshTokenExpireTime);
        return new String[]{accessToken, refreshToken};
    }

    //RefreshToken -> AccessToken 재발급
    public String renewAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new JwtException("Refresh Token이 만료되었습니다. 다시 로그인하세요.");
        }

        Claims claims = parseClaims(refreshToken, refreshKey);
        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        return generateToken(new JwtUserInfoDto(userId, role), accessKey, accessTokenExpireTime);
    }

    // JWT 생성
    private String generateToken(JwtUserInfoDto user, Key key, long expireTime) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // AccessToken 검증
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessKey);
    }

    //RefreshToken 검증
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey);
    }

    // 토큰 검증
    private boolean validateToken(String token, Key key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    //JWT에서 Claims 추출
    private Claims parseClaims(String token, Key key) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    //토큰에서 userId 가져오기
    public String getUserIdFromToken(String token) {
        return parseClaims(token, accessKey).getSubject();
    }

}
