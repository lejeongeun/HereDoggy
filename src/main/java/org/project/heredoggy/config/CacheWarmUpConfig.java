package org.project.heredoggy.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CacheWarmUpConfig {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void clearRedisCacheOnStartup() {
        Set<String> keys = redisTemplate.keys("heredoggy::cache::*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            System.out.println("🧹 Redis 캐시 초기화 완료 (" + keys.size() + "개)");
        }
    }
}