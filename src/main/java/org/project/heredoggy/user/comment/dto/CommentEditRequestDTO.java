package org.project.heredoggy.user.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.post.comment.PostType;

@Getter
@NoArgsConstructor
public class CommentEditRequestDTO {
    private String content;
}
