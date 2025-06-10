package org.project.heredoggy.user.walk.reservation.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnavailableTimeResponseDTO {
    private LocalDate date;
    private boolean morningUnavailable;
    private boolean afternoonUnavailable;
}
