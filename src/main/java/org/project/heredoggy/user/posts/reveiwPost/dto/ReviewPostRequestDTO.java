package org.project.heredoggy.user.posts.reveiwPost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostType;

@Getter
@NoArgsConstructor
public class ReviewPostRequestDTO {
    private String title;
    private String content;
    private ReviewPostType type;
    private Integer rank;
}
