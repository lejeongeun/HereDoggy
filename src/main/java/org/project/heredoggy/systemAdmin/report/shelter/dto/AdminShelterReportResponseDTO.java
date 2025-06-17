package org.project.heredoggy.systemAdmin.report.shelter.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminShelterReportResponseDTO {
    private Long id;
    private Long shelterId;
    private String shelterNameSnapshot;
    private String reporterNickname;
    private String reason;
    private ReportStatus status;
    private LocalDateTime reportedAt;
}
