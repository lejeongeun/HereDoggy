package org.project.heredoggy.user.adoption.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.adoption.AdoptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberAdoptionResponseDTO {
    private Long adoptionId;
    private Long dogId;
    private String dogName;
    private String shelterName;
    private String shelterPhone;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private LocalDateTime createdAt;
    private AdoptionStatus status;

}
