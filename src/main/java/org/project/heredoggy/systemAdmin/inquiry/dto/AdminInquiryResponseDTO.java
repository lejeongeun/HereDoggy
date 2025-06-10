package org.project.heredoggy.systemAdmin.inquiry.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.inquiry.InquiryStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminInquiryResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String senderName;
    private String senderEmail;
    private String imageUrl;
    private InquiryStatus status;
    private LocalDateTime createdAt;
}
