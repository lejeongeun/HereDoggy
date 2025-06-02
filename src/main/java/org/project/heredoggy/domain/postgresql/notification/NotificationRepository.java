package org.project.heredoggy.domain.postgresql.notification;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverOrderByCreatedAtDesc(Member member);

    List<Notification> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(Member member);
    List<Notification> findByReceiverAndIsReadFalse(Member member);

    List<Notification> findByCreatedAtBefore(LocalDateTime dateTime);
}
