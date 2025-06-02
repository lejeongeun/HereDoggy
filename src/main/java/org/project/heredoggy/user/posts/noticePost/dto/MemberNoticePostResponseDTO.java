package org.project.heredoggy.user.posts.noticePost.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberNoticePostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private String email;
    private String nickname;
    private String createdAt;
    private List<String> imageUrls;
}
