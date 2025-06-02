package org.project.heredoggy.user.adoption.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberAdoptionRequestDTO {

    @NotNull(message = "결혼 여부를 입력해 주세요.")
    private boolean isMarried;

    @NotNull(message = "방문 날짜를 입력해 주세요")
    private LocalDate visitDate;

    @NotNull(message = "방문 시간을 입력해 주세요.")
    private LocalTime visitTime;

    @Valid
    @NotNull(message = "설문서를 작성해 주세요.")
    private AdoptionSurveyRequestDTO survey;

}
