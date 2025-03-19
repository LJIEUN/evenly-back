package com.codeisevenlycooked.evenly.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisServiceTest {
    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void logout_success_add_to_blacklist() {
        String token = "testToken";
        long expirationTime = 60000; //1분

        redisService.add(token, "blacklisted", expirationTime);

        verify(valueOperations, times(1)).set(eq(token), eq("blacklisted"), eq(expirationTime), eq(TimeUnit.MILLISECONDS));

    }

    @Test
    void cannot_authenticate_with_blacklisted_token() {
        String token = "testToken";
        when(redisTemplate.hasKey(token)).thenReturn(true);

        boolean result = redisService.isBlackListed(token);

        assertTrue(result, "로그아웃된 토큰은 인증되면 안됩니다.");
    }
}