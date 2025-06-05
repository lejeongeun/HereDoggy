package org.project.heredoggy.shelter.walk.unavailableDate.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UnavailableDateResponseDTO {
    private Long id;
    private List<LocalDate> dates;
}
