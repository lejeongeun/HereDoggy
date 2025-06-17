package org.project.heredoggy.user.report.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReport;
import org.project.heredoggy.domain.postgresql.report.member.MemberReport;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReport;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyReportResponseDTO {
    private Long id;
    private Long reportedId;
    private String nickname;
    private String reasonContent;
    private String status;
    private LocalDateTime createdAt;

    public static MyReportResponseDTO fromCommentReport(CommentReport report) {
        return new MyReportResponseDTO(
                report.getId(),
                report.getComment().getId(),
                report.getComment().getWriter().getNickname(),
                report.getReason().getContent(),
                report.getStatus().name(),
                report.getCreatedAt()
        );
    }

    public static MyReportResponseDTO fromShelterReport(ShelterReport report) {
        return new MyReportResponseDTO(
                report.getId(),
                report.getShelter().getId(),
                report.getShelter().getName(),
                report.getReason().getContent(),
                report.getStatus().name(),
                report.getCreatedAt()
        );
    }

    public static MyReportResponseDTO fromMemberReport(MemberReport report) {
        return new MyReportResponseDTO(
                report.getId(),
                report.getReported().getId(),
                report.getReported().getNickname(),
                report.getReason().getContent(),
                report.getStatus().name(),
                report.getCreatedAt()
        );
    }
}
