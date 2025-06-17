package org.project.heredoggy.global.notification;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notification.NotificationType;
import org.project.heredoggy.domain.postgresql.notification.ReferenceType;
import org.project.heredoggy.user.notification.service.NotificationSseService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSseNotificationFactory {
    private final NotificationSseService notificationSseService;

    /**
     * 보호소 신고 알림 전송
     * @param admin             시스템 관리자 계쩡
     * @param reporterNickname  신고자 이름
     * @param reasonContent     이유
     * @param shelterId         신고 관련 ID
     *
     */
    public void notifyShelterReported(Member admin, String reporterNickname, String reasonContent, Long shelterId) {
        String title = "신고 알림";
        String content = "🚨 " + reporterNickname + "님이 보호소를 신고했습니다.\n사유: " + reasonContent;

        notificationSseService.sendNotification(
                admin,
                title,
                content,
                NotificationType.REPORT,
                ReferenceType.SHELTER,
                shelterId
        );
    }

    /**
     * 유저 신고 알림 전송
     * @param admin             시스템 관리자 계쩡
     * @param reporterNickname  신고자 이름
     * @param reasonContent     이유
     * @param commentId         댓글 관련 ID
     *
     */
    public void notifyCommentReported(Member admin, String reporterNickname, String reasonContent, Long commentId) {
        String title = "댓글 신고 알림";
        String content = "🚨 " + reporterNickname + "님이 댓글을 신고했습니다.\n사유: " + reasonContent;

        notificationSseService.sendNotification(
                admin,
                title,
                content,
                NotificationType.REPORT,
                ReferenceType.COMMENT,
                commentId
        );
    }

    /**
     * 유저 신고 알림 전송
     * @param admin             시스템 관리자 계쩡
     * @param reporterNickname  신고자 이름
     * @param reasonContent     이유
     * @param type              게시물 타입
     * @param postId            신고 관련 ID
     *
     */
    public void notifyPostReported(Member admin, String reporterNickname, String reasonContent, PostType type, Long postId) {
        String title = "게시물 신고 알림";
        String content = "🚨 " + reporterNickname + "님이 해당 게시물을 신고했습니다.\n사유: " + reasonContent;

        notificationSseService.sendNotification(
                admin,
                title,
                content,
                NotificationType.REPORT,
                convertPostTypeToReferenceType(type),
                postId
        );
    }

    /**
     * 유저 신고 알림 전송
     * @param admin             시스템 관리자 계쩡
     * @param reporterNickname  신고자 이름
     * @param reportedNickname  신고 당한 이름
     * @param reasonContent     이유
     * @param reportedId         신고 관련 ID
     *
     */
    public void notifyMemberReported(Member admin, String reporterNickname, String reportedNickname, String reasonContent, Long reportedId) {
        String title = "신고 알림";
        String content = "🚨 " + reporterNickname + "님이 " + reportedNickname + "를 신고했습니다.\n사유: " + reasonContent;

        notificationSseService.sendNotification(
                admin,
                title,
                content,
                NotificationType.REPORT,
                ReferenceType.MEMBER,
                reportedId
        );
    }

    private ReferenceType convertPostTypeToReferenceType(PostType postType) {
        return switch (postType) {
            case FREE -> ReferenceType.FREE_POST;
            case MISSING -> ReferenceType.MISSING_POST;
            case REVIEW -> ReferenceType.REVIEW_POST;
            case NOTICE -> ReferenceType.NOTICE_POST;
        };
    }
}