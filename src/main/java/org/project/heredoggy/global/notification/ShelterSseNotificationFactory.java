package org.project.heredoggy.global.notification;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notification.NotificationType;
import org.project.heredoggy.domain.postgresql.notification.ReferenceType;
import org.project.heredoggy.user.notification.service.NotificationSseService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShelterSseNotificationFactory {

    private final NotificationSseService notificationSseService;

    /**
     * 보호소 관리자에게 산책 예약 알림을 전송합니다.
     *
     * @param shelterAdmin 보호소 관리자 계정
     * @param dogName      예약된 강아지 이름
     * @param memberName   예약 신청자 이름
     */
    public void notifyWalkReservation(Member shelterAdmin, String dogName, String memberName, Long reservationId) {
        String title = "산책 예약 요청";
        String content = "🚶 " + memberName + "님이 " + dogName + " 산책을 신청했습니다.";
        notificationSseService.sendNotification(
                shelterAdmin,
                title,
                content,
                NotificationType.WALK_RESERVATION,
                ReferenceType.WALK_RESERVATION,
                reservationId
        );
    }

    public void notifyWalkReservationCanceled(Member shelterAdmin, String dogName, String memberName, Long reservationId) {
        String title = "산책 예약 취소 요청";
        String content = "❌ " + memberName + "님이 " + dogName + " 산책 신청을 취소 요청했습니다.";
        notificationSseService.sendNotification(
                shelterAdmin,
                title,
                content,
                NotificationType.WALK_RESERVATION,
                ReferenceType.WALK_RESERVATION,
                reservationId
        );
    }

    /**
     * 보호소 관리자에게 입양 신청 알림을 전송합니다.
     *
     * @param shelterAdmin 보호소 관리자 계정
     * @param dogName      입양 신청한 강아지 이름
     * @param memberName   신청자 이름
     */
    public void notifyAdoptionRequest(Member shelterAdmin, String dogName, String memberName, Long adoptionId) {
        String title = "입양 신청";
        String content = "🏠 " + memberName + "님이 " + dogName + "의 입양을 신청했습니다.";
        notificationSseService.sendNotification(
                shelterAdmin,
                title,
                content,
                NotificationType.ADOPTION_REQUEST,
                ReferenceType.ADOPTION,
                adoptionId
        );
    }



}