package org.project.heredoggy.user.chat.limit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ChatRateLimiter {

    private final StringRedisTemplate redisTemplate;
    private static final int DAILY_LIMIT = 20;

    public boolean isLimitExceeded(Long memberId) {
        String key = buildKey(memberId);
        String currentCountStr = redisTemplate.opsForValue().get(key);
        int currentCount = currentCountStr != null ? Integer.parseInt(currentCountStr) : 0;
        return currentCount >= DAILY_LIMIT;
    }

    public void increment(Long memberId) {
        String key = buildKey(memberId);
        Long newCount = redisTemplate.opsForValue().increment(key);
        if (newCount != null && newCount == 1) {
            redisTemplate.expire(key, Duration.ofDays(1));
        }
    }

    public int getRemaining(Long memberId) {
        String key = buildKey(memberId);
        String countStr = redisTemplate.opsForValue().get(key);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        return DAILY_LIMIT - count;
    }

    private String buildKey(Long memberId) {
        return "chat_limit:" + memberId + ":" + LocalDate.now();
    }
}