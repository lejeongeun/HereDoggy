package org.project.heredoggy.systemAdmin.report.post.dto;

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
public class AdminPostReportDetailDTO {
    private Long id;
    private Long postId;
    private String postTitleSnapshot;
    private String postContentSnapshot;
    private String reporterNickname;
    private String writerNickname;
    private String reason;
    private ReportStatus status;
    private String adminMemo;
    private LocalDateTime reportedAt;
}
