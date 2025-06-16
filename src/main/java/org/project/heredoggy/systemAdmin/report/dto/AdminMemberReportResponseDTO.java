package org.project.heredoggy.systemAdmin.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMemberReportResponseDTO {
    private Long reportId;
    private Long reportedCommentId;
    private String reporterNickname;
    private String commentContent;
    private String commentWriterNickname;
    private String reason;
    private String status;
    private LocalDateTime reportedAt;
}
