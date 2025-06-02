package org.project.heredoggy.user.posts.reveiwPost.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPostResponseDTO {
    private Long id;
    private String title;
    private ReviewPostType type;
    private String content;
    private Integer rank;
    private Long viewCount;
    private String email;
    private String nickname;
    private String createdAt;

    private List<String> imageUrls;
}
