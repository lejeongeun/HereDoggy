package org.project.heredoggy.domain.postgresql.fcm;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMemberAndToken(Member member, String token);
    void deleteByToken(String token);

    List<FcmToken> findAllByMember(Member member);
}
