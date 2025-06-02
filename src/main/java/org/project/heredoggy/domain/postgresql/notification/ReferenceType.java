package org.project.heredoggy.domain.postgresql.notification;

public enum ReferenceType {
    // 게시글
    FREE_POST,
    REVIEW_POST,
    MISSING_POST,
    NOTICE_POST,        // 시스템 or 보호소 공지

    // 행위 대상
    COMMENT,
    MEMBER,
    DOG,

    // 신청/요청
    WALK_RESERVATION,
    ADOPTION_REQUEST,

    // 처리 결과
    ADOPTION,
    SYSTEM_NOTICE
}
