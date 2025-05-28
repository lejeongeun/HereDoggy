package org.project.heredoggy.domain.postgresql.walk.reservation;

public enum WalkReservationStatus {
    PENDING, // 대기중
    APPROVED, // 승인
    REJECTED, // 거절
    CANCELED, // 취소됨
    CANCELED_REQUEST, // 사용자 취소 요청
    COMPLETED // 산책 완료됨

}
