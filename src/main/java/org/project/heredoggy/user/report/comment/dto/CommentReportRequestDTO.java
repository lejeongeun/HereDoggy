package org.project.heredoggy.user.report.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentReportRequestDTO {
    private Long commentId;
    private Long reasonId;
}
