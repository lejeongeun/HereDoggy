package org.project.heredoggy.user.walk.reservation.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.walk.reservation.WalkReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberReservationResponseDTO {
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String note; // 메모

    private Long dogId;
    private String dogName;

    private Long shelterId;
    private String shelterName;

    private WalkReservationStatus walkReservationStatus;
    private LocalDateTime createAt;

}