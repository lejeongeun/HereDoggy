package org.project.heredoggy.user.report.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;

@Getter
@NoArgsConstructor
public class PostReportRequestDTO {
    private Long postId;
    private PostType postType;
    private Long reasonId;
}