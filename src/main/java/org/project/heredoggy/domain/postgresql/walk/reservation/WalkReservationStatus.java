package org.project.heredoggy.domain.postgresql.walk.reservation;

public enum WalkReservationStatus {
    PENDING, // 대기중
    APPROVED, // 승인
    REJECTED, // 거절
    CANCELED, // 사용자 취소
    COMPLETED // 산책 완료됨

}
