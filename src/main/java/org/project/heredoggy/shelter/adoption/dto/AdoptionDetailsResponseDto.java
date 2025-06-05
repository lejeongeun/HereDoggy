package org.project.heredoggy.shelter.adoption.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.adoption.AdoptionStatus;
import org.project.heredoggy.user.adoption.dto.AdoptionSurveyResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionDetailsResponseDto {
    private Long adoptionId;

    private Long memberId;
    private String memberName;
    private String memberPhone;

    private Long dogId;
    private String dogName;

    private LocalDate visitDate;
    private LocalTime visitTime;

    private LocalDateTime createdAt;
    private AdoptionStatus status;

    private LocalDateTime decisionAt; // 관리자 승인 시간
    private AdoptionSurveyResponseDTO survey;


}
