package org.project.heredoggy.systemAdmin.report.member.dto;

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
public class AdminMemberReportResponseDTO {
    private Long id;
    private Long reportedMemberId;
    private String memberNicknameSnapshot;
    private String reporterNickname;
    private String reason;
    private ReportStatus status;
    private LocalDateTime reportedAt;
}
