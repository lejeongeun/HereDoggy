package org.project.heredoggy.user.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.domain.postgresql.fcm.FcmToken;
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

    //알림 보내기
    public void sendNotification(Member receiver, NotificationType type,
                                 ReferenceType referenceType, Long referenceId,
                                 String title, String content) {
        Notification notification = Notification.builder()
                .receiver(receiver)
                /* COMMENT(댓글), LIKE(좋아요), WALK_RESERVATION(산책 예약), WALK_RESULT(산책 결과),
                ADOPTION_REQUEST(입양 예약), ADOPTION_RESULT(입양 결과), REPORT_RESULT(신고 결과), SYSTEM_NOTICE(공지사항) */
                .type(type)
                /* FREE_POST, REVIEW_POST,MISSING_POST, NOTICE_POST,
                COMMENT(댓글), WALK_RESERVATION(산책 예약),
                ADOPTION(입양 결과), SYSTEM_NOTICE(공지사항) */
                .referenceType(referenceType)
                .referenceId(referenceId)
                .title(title)
                .content(content)
                .isRead(false)
                .build();
        //알림 저장
        notificationRepository.save(notification);

        // 사용자가 푸시 수신 거부 했을경우 생략
        if (!receiver.getIsNotificationEnabled()) return;

        //해당 유저가 로그인한 모든 기기의 FCM 토큰 불러옴
        List<FcmToken> tokens = fcmTokenRepository.findAllByMember(receiver);

        //각 기기마다 푸시 전송 시도
        for (FcmToken token : tokens) {
            try {
                Message message = Message.builder()
                        .setToken(token.getToken())      //푸시를 보낼 대상 기기
                        .setNotification(com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)        // 제목
                                .setBody(content)       // 내용
                                .build())
                        .putData("referenceType", referenceType.name()) //알림 이동을 위한 타입
                        .putData("referenceId", referenceId.toString()) //알림 이동을 위한 ID
                        .build();

                FirebaseMessaging.getInstance().send(message); //FCM 전송
                //전송 실패 처리
            } catch (FirebaseMessagingException e) {
                String errorCode = e.getErrorCode().name();

                if ("UNREGISTERED".equals(errorCode) ||                     // 앱 삭제됨, 토큰 만료
                        "INVALID_ARGUMENT".equals(errorCode) ||             // 토큰이 잘못된 형식
                        "INVALID_REGISTRATION_TOKEN".equals(errorCode)) {   // 존재하지 않는 토큰

                    fcmTokenRepository.deleteByToken(token.getToken());     // 무효 토큰이면 삭제
                    log.warn("무효 FCM 토큰 삭제됨: {}", token.getToken());
                }

                log.error("FCM 전송 실패 [{}]: {}", errorCode, token.getToken());
            }
        }
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
