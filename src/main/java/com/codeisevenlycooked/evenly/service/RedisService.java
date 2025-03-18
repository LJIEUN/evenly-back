package com.codeisevenlycooked.evenly.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    //블랙리스트에 토큰 저장(로그아웃)
    public void add(String token, String value, long expiration) {
        redisTemplate.opsForValue().set(token, value, expiration, TimeUnit.MILLISECONDS);
    }

    //리스트에 토큰 확인
    public boolean isBlackListed(String token) {
        return redisTemplate.hasKey(token);
    }

    public void remove(String token) {
        redisTemplate.delete(token);
    }
}
