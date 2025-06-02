package org.project.heredoggy.user.posts.reveiwPost.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostType;

import java.util.List;


@Getter
@NoArgsConstructor
public class ReviewPostEditRequestDTO {
    private String title;
    private String content;
    private ReviewPostType type;
    private Integer rank;
    private List<Long> deleteImageIds;
}