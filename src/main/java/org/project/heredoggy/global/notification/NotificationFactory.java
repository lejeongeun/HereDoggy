package org.project.heredoggy.global.notification;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notification.NotificationType;
import org.project.heredoggy.domain.postgresql.notification.ReferenceType;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.user.notification.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationFactory {
    private final NotificationService notificationService;

    /**
     * 댓글 작성 시 수신자에게 알림전송
     *
     * - 작성자와 수신자가 동일하지 않은 경우에만 알림을 발송합니다.
     * - 알림 유형은 COMMENT이며, 참조 타입과 게시물 ID를 함께 전달합니다.
     *
     * @param receiver   알림을 받을 회원
     * @param commenter  댓글을 작성한 회원
     * @param type       참조 대상 타입 (예: FREE_POST, REVIEW_POST 등)
     * @param postId     댓글이 작성된 게시글 ID
     */
    public void notifyComment(Member receiver, Member commenter, ReferenceType type, Long postId) {
        if(!receiver.equals(commenter)) {
            notificationService.sendNotification(
                    receiver,
                    NotificationType.COMMENT,
                    type,
                    postId,
                    "💬 새 댓글 알림",
                    commenter.getNickname() + "님이 회원님의 게시물에 댓글을 달았습니다."
            );
        }
    }

    //좋아요 알림
    public void notifyLike(Member receiver, Member liker, ReferenceType type, Long postId) {
        if(!receiver.equals(liker)) {
            notificationService.sendNotification(
                    receiver,
                    NotificationType.LIKE,
                    type,
                    postId,
                    "♥️ 좋아요 알림",
                    liker.getNickname() + "님이 회원님의 게시글을 좋아합니다."
            );
        }
    }

    public void notifyWalkResult(Member receiver, boolean isApproved, Long reservationId) {
        String title = isApproved ? "🚶 산책 예약 승인" : "🚫 산책 예약 거절";
        String content = isApproved
                ? "보호소에서 회원님의 산책 예약을 승인했어요! 마이페이지에서 확인해보세요."
                : "보호소에서 회원님의 산책 예약을 거절했어요. 다른 일정을 시도해보세요.";

        notificationService.sendNotification(
                receiver,
                NotificationType.WALK_RESULT,
                ReferenceType.WALK_RESERVATION,
                reservationId,
                title,
                content
        );
    }




}
