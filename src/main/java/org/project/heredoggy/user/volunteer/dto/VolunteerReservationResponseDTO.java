package org.project.heredoggy.user.volunteer.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.volunteer.VolunteerReservation;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerReservationResponseDTO {
    private Long reservationId;
    private String shelterName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isGroup;
    private String status;

    public static VolunteerReservationResponseDTO fromEntity(VolunteerReservation reservation) {
        Shelter shelter = reservation.getShelter();

        return VolunteerReservationResponseDTO.builder()
                .reservationId(reservation.getId())
                .shelterName(shelter.getName())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .isGroup(reservation.getIsGroup())
                .status(reservation.getStatus().name())
                .build();
    }
}
