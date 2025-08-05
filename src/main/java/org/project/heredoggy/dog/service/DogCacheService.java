package org.project.heredoggy.dog.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.MainDogResponseDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DogCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String DOG_DETAIL_KEY_PREFIX = "dog:detail:"; // Redis key 접두사
    private static final long TTL = 5 * 60; // TTL 5분 (초 단위)
    // 1. 캐시에서 강아지 상세 정보 조회
    public MainDogResponseDTO getDogDetailFromCache(Long dogId){
        String key = DOG_DETAIL_KEY_PREFIX + dogId;
        return (MainDogResponseDTO) redisTemplate.opsForValue().get(key);
    }

    // 2. 캐시에 저장
    public void setDogDetailToCache(Long dogId, MainDogResponseDTO dto){
        String key = DOG_DETAIL_KEY_PREFIX + dogId;

        redisTemplate.opsForValue().set(key, dto, TTL, TimeUnit.SECONDS);
    }

    // 3. 캐시 삭제
    public void evictCache(Long dogId){
        redisTemplate.delete(DOG_DETAIL_KEY_PREFIX + dogId);
    }

}
