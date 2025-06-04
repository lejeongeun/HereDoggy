package org.project.heredoggy.user.fcm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.domain.postgresql.fcm.FcmToken;
import org.project.heredoggy.domain.postgresql.fcm.FcmTokenRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void saveOrUpdate(String token, Member member) {
        Optional<FcmToken> existing = fcmTokenRepository.findByMemberAndToken(member, token);

        if (existing.isEmpty()) {
            FcmToken newToken = FcmToken.builder()
                    .member(member)
                    .token(token)
                    .build();
            fcmTokenRepository.save(newToken);
        } else {
            log.info("이미 등록된 FCM 토큰입니다. token={}, memberId={}", token, member.getId());
        }
    }


    @Transactional
    public void deleteToken(String token, Member member) {
        FcmToken tokenEntity = fcmTokenRepository.findByMemberAndToken(member, token)
                .orElseThrow(() -> new IllegalArgumentException("해당 FCM 토큰이 존재하지 않습니다."));

        if (!tokenEntity.getMember().getId().equals(member.getId())) {
            throw new SecurityException("다른 사용자의 토큰은 삭제할 수 없습니다.");
        }

        fcmTokenRepository.delete(tokenEntity);
    }

}
