package org.project.heredoggy.shelter.walk.unavailableDate.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnavailableDateRequestDTO {
    private List<LocalDate> dates;
}
