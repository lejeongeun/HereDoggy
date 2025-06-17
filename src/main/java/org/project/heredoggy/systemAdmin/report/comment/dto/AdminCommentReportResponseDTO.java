package org.project.heredoggy.systemAdmin.report.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCommentReportResponseDTO {
    private Long id;
    private Long commentId;
    private String commentContentSnapshot;
    private String reporterNickname;
    private String writerNickname;
    private String reason;
    private ReportStatus status;
    private LocalDateTime reportedAt;
}
