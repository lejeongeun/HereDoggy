package org.project.heredoggy.user.posts.freePost.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreePostSummaryResponseDTO {
    private Long id;
    private String title;
    private String nickname;
    private String previewContent;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private String thumbnailImageUrl;
    private LocalDateTime createdAt;
}