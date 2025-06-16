package org.project.heredoggy.user.report.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.post.PostReport;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReportResponseDTO {
    private Long postId;
    private String postType;
    private String reasonContent;
    private ReportStatus status;
    private LocalDateTime createdAt;

    public static PostReportResponseDTO fromEntity(PostReport report) {
        return new PostReportResponseDTO(
                report.getPostId(),
                report.getPostType().name(),
                report.getReason().getContent(),
                report.getStatus(),
                report.getCreatedAt()
        );
    }
}
