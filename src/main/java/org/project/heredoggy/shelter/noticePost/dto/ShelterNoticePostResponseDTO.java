package org.project.heredoggy.shelter.noticePost.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelterNoticePostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Long viewCount;
    private String email;
    private String nickname;
    private String createdAt;
}
