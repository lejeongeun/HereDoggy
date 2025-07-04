package org.project.heredoggy.user.volunteer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VolunteerReservationRequestDTO {
    private Long shelterId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isGroup;
}
