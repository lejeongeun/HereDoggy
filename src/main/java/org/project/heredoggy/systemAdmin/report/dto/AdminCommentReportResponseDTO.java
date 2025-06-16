package org.project.heredoggy.systemAdmin.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCommentReportResponseDTO {
    private Long reportedId;
    private String reportedNickname;
    private String commentContent;
    private String reason;
    private ReportStatus status;
    private LocalDateTime createdAt;
}
