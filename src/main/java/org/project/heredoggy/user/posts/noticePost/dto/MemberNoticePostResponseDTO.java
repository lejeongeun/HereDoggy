package org.project.heredoggy.user.posts.noticePost.dto;

import lombok.*;

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
}
