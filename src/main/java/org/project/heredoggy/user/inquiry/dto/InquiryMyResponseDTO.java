package org.project.heredoggy.user.inquiry.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.inquiry.InquiryStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryMyResponseDTO {
    private Long id;
    private String title;
    private String content;
    private List<String> imageUrls;
    private InquiryStatus status;
    private LocalDateTime createdAt;
}
