package org.project.heredoggy.domain.postgresql.post.like;

public interface LikeCountProjection {
    Long getPostId();
    Long getCnt();
}
