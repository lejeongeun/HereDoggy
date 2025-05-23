package org.project.heredoggy.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "RT:";

    public void saveRefreshToken(Long memberId, String refreshToken, long expirationMillis) {
        String key = PREFIX + memberId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expirationMillis));
    }

    public String getRefreshToken(Long memberId) {
        return redisTemplate.opsForValue().get(PREFIX + memberId);
    }

    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(PREFIX + memberId);
    }
}
