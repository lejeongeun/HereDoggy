package org.project.heredoggy.user.comment.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.post.comment.PostType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    private Long id;
    private PostType postType;
    private Long postId;
    private String content;
    private String email;
    private String nickname;
    private String createdAt;
}