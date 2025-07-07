package org.project.heredoggy.user.posts.noticePost.dto;

public interface MemberNoticePostResDTO {
    Long getId();
    String getTitle();
    String getContent();
    int getViewCount();
    String getCreatedAt();
    String getEmail();
    String getNickname();
    Long getLikeCount();
    String getImageUrl();
}
