package org.project.heredoggy.user.volunteer.dto;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerUnavailableTimeDTO {
    private LocalTime startTime;
    private LocalTime endTime;
}
