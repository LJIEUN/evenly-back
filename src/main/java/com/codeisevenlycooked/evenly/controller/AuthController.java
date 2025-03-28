package com.codeisevenlycooked.evenly.controller;

import com.codeisevenlycooked.evenly.config.security.JwtUtil;
import com.codeisevenlycooked.evenly.dto.SignInDto;
import com.codeisevenlycooked.evenly.dto.SignUpDto;
import com.codeisevenlycooked.evenly.dto.TokenResponse;
import com.codeisevenlycooked.evenly.global.MessageResponse;
import com.codeisevenlycooked.evenly.service.AuthService;
import com.codeisevenlycooked.evenly.service.RedisService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        authService.signUp(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("회원가입 성공!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid SignInDto signInDto) {
        String[] tokens = authService.login(signInDto);
        return ResponseEntity.ok()
                .body(new TokenResponse(tokens[0], tokens[1]));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.resolveToken(request);

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("messsage", "Refresh Token is missing"));
        }
        try {
            String newAccessToken = jwtUtil.renewAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("messsage", "Invalid Token"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestHeader("Authorization") String token) {
        String accessToken = token.replace("Bearer ", "");
        long expiration = jwtUtil.getExpiredTime(accessToken).getTime() - System.currentTimeMillis();

        if (expiration > 0) {
            redisService.add(accessToken, "blacklisted", expiration);
        }

        return ResponseEntity.ok(new MessageResponse("로그아웃 완료!"));
    }
}
