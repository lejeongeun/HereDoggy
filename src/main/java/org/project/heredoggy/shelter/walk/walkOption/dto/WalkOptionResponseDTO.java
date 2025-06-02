package org.project.heredoggy.shelter.walk.walkOption.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.dog.Dog;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalkOptionResponseDTO {
    private Long id;
    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long dogsId;
}
