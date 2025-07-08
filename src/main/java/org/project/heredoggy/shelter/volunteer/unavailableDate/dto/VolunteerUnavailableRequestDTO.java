package org.project.heredoggy.shelter.volunteer.unavailableDate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class VolunteerUnavailableRequestDTO {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
}
