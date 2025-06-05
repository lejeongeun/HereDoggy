package org.project.heredoggy.domain.postgresql.notification;

public enum NotificationType {
    // 유저 활동
    COMMENT,
    LIKE,

    // 산책 관련
    WALK_RESERVATION,       // 유저 → 보호소 (신청)
    WALK_RESULT,            // 보호소 → 유저 (승인/거절 등)

    // 입양 관련
    ADOPTION_REQUEST,       // 유저 → 보호소
    ADOPTION_RESULT,        // 보호소 → 유저

    // 신고, 시스템
    REPORT_RESULT,
    SYSTEM_NOTICE
}
