package org.project.heredoggy.shelter.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.project.heredoggy.domain.postgresql.inquiry.InquiryStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ShelterInquiryDetailResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String senderName;
    private String senderEmail;
    private List<String> imageUrls;
    private InquiryStatus status;
    private LocalDateTime createdAt;
}
