package org.project.heredoggy.user.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.domain.postgresql.fcm.FcmTokenRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notification.Notification;
import org.project.heredoggy.domain.postgresql.notification.NotificationRepository;
import org.project.heredoggy.domain.postgresql.notification.NotificationType;
import org.project.heredoggy.domain.postgresql.notification.ReferenceType;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.exception.UnauthorizedException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.notification.dto.NotificationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public void sendNotification(Member receiver,
                                 NotificationType type,
                                 ReferenceType referenceType,
                                 Long referenceId,
                                 String title,
                                 String content) {
        Notification notification = Notification.builder()
                .receiver(receiver)
                .type(type)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .title(title)
                .content(content)
                .isRead(false)
                .build();
        notificationRepository.save(notification);

        if(!receiver.getIsNotificationEnabled()) return;

        fcmTokenRepository.findByMember(receiver).ifPresent(token -> {
            try {
                Message message = Message.builder()
                        .setToken(token.getToken())
                        .setNotification(com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(content)
                                .build())
                        .putData("referenceType", referenceType.name())
                        .putData("referenceId", referenceId.toString())
                        .build();

                FirebaseMessaging.getInstance().send(message);
            } catch (Exception e) {
                log.error("FCM 알림 전송 실패", e);
            }
        });
    }

///////////////////////////////////////////////////////////

    // 전체 알림 리스트 조회
    public List<NotificationResponseDTO> getNotificationsFor(Member member) {
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(member)
                .stream()
                .map(n -> NotificationResponseDTO.builder()
                        .id(n.getId())
                        .title(n.getTitle())
                        .content(n.getContent())
                        .type(n.getType())
                        .referenceType(n.getReferenceType())
                        .referenceId(n.getReferenceId())
                        .isRead(n.getIsRead())
                        .createdAt(n.getCreatedAt())
                        .build())
                .toList();
    }

    // 안 읽은 알림 리스트 조회
    public List<NotificationResponseDTO> getUnreadNotificationsFor(Member member) {
        return notificationRepository.findByReceiverAndIsReadFalseOrderByCreatedAtDesc(member)
                .stream()
                .map(n -> NotificationResponseDTO.builder()
                        .id(n.getId())
                        .title(n.getTitle())
                        .content(n.getContent())
                        .type(n.getType())
                        .referenceType(n.getReferenceType())
                        .referenceId(n.getReferenceId())
                        .isRead(n.getIsRead())
                        .createdAt(n.getCreatedAt())
                        .build())
                .toList();
    }

    // 알림 전체 읽음 처리
    public void markAllAsRead(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        List<Notification> notifications = notificationRepository.findByReceiverAndIsReadFalse(member);
        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    // 개별 알림 읽음 처리
    public void markAsRead(Long id, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("알림이 존재하지 않습니다."));

        if (!notification.getReceiver().getId().equals(member.getId())) {
            throw new UnauthorizedException("본인의 알림만 읽음 처리할 수 있습니다.");
        }

        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }

    // 알림 삭제
    public void deleteNotification(Long id, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("알림이 존재하지 않습니다."));

        if(!notification.getReceiver().getId().equals(member.getId())) {
            throw new UnauthorizedException("본인의 알림만 삭제할 수 있습니다.");
        }
        notificationRepository.delete(notification);
    }


    @Transactional
    public void deleteOldNotifications() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        List<Notification> oldNotifications = notificationRepository.findByCreatedAtBefore(threshold);
        notificationRepository.deleteAll(oldNotifications);
    }

}
