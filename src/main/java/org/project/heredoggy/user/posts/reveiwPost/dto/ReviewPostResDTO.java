package org.project.heredoggy.user.posts.reveiwPost.dto;

import java.util.List;

public interface ReviewPostResDTO {
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
