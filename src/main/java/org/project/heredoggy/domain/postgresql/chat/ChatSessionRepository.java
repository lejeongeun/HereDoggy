package org.project.heredoggy.domain.postgresql.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findTopByMemberIdOrderByCreatedAtDesc(Long memberId);
}