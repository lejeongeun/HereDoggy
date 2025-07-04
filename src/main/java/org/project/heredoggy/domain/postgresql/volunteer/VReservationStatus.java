package org.project.heredoggy.domain.postgresql.volunteer;

public enum VReservationStatus {
    PENDING,     // 대기중
    ACCEPTED,    // 승인됨
    REJECTED,     // 거절됨
    CANCELED,      // 취소됨
    CANCEL_REQUEST //취소 요청
}
