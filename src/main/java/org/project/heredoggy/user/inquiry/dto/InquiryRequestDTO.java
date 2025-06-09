package org.project.heredoggy.user.inquiry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryRequestDTO {
    private String title;
    private String content;
    private Long targetId;
}
