package org.project.heredoggy.user.posts.freePost.dto;


public interface FreePostResDTO {
    Long getId();
    String getTitle();
    String getContent();
    int getViewCount();
    String getCreatedAt();
    String getEmail();
    String getNickname();
    int getLikeCount();
    String getImageUrl();
}
