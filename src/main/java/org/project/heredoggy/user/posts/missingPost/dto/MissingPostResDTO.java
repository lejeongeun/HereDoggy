package org.project.heredoggy.user.posts.missingPost.dto;

public interface MissingPostResDTO {
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
