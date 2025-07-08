package org.project.heredoggy.user.posts.freePost.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreePostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private String email;
    private String nickname;
    private String createdAt;
    private List<String> imagesUrls;
    private Long commentCount;
    private Long likeCount;
}
