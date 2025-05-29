package org.project.heredoggy.walk.walkOption.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalkOptionRequestDTO {

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

}
