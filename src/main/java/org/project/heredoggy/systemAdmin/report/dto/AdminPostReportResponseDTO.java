package org.project.heredoggy.systemAdmin.report.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.comment.PostType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPostReportResponseDTO {
    private Long id;
    private String postTitle;
    private String reporterNickname;
    private String reportedNickname;
    private String reasonContent;
    private String status;
    private PostType type;
    private LocalDateTime reportedAt;
}
