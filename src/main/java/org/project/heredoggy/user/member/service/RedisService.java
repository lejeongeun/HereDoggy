package org.project.heredoggy.user.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "RT:";


    //Refresh Token 발급
    public void saveRefreshToken(String email, String refreshToken, long expirationMillis) {
        String key = PREFIX + email;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expirationMillis));
    }

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(PREFIX + email);
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete(PREFIX + email);
    }


    //비밀번호 재설정
    public void savePasswordResetToken(String email, String token) {
        redisTemplate.opsForValue().set("reset:" + token, email, Duration.ofMinutes(30));
    }

    public String getEmailByResetToken(String token) {
        return redisTemplate.opsForValue().get("reset:" + token);
    }

    public void deletePasswordResetToken(String token) {
        redisTemplate.delete("reset:" + token);
    }



    //회원가입 이메일 인증
    //redis에 저장하기
    public void saveEmailVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set("verify:" + email, code, Duration.ofMinutes(5));
    }

    //
    public String getEmailVerificationCode(String email) {
        return redisTemplate.opsForValue().get("verify:" + email);
    }

    public void deleteEmailVerificationCode(String email) {
        redisTemplate.delete("verify:" + email);
    }
}
