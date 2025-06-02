package org.project.heredoggy.user.fcm.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.fcm.FcmToken;
import org.project.heredoggy.domain.postgresql.fcm.FcmTokenRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void saveOrUpdate(String token, Member member) {
        FcmToken existing = fcmTokenRepository.findByMember(member).orElse(null);
        if (existing != null) {
            existing.setToken(token);
        } else {
            FcmToken newToken = FcmToken.builder()
                    .token(token)
                    .member(member)
                    .build();
            fcmTokenRepository.save(newToken);
        }
    }

    @Transactional
    public void deleteByMember(Member member) {
        fcmTokenRepository.deleteByMember(member);
    }
}
