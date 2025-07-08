package org.project.heredoggy.shelter.noticePost.dto;

public interface NoticePostResDTO {
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
