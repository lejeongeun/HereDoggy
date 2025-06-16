package org.project.heredoggy.systemAdmin.report.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminShelterReportResponseDTO {
    private Long reportId;
    private Long reportedCommentId;
    private String reporterNickname;
    private String commentContent;
    private String commentWriterNickname;
    private String reason;
    private String status;
    private LocalDateTime reportedAt;
}
