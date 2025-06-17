package org.project.heredoggy.systemAdmin.report.post.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPostReportResponseDTO {
    private Long id;
    private String postTitleSnapshot;
    private String postContentSnapshot;
    private String reporterNickname;
    private String writerNickname;
    private String reason;
    private ReportStatus status;
    private PostType type;
    private Long postId;
    private LocalDateTime reportedAt;
}
