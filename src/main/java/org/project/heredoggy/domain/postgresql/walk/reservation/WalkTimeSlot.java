package org.project.heredoggy.domain.postgresql.walk.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public enum WalkTimeSlot {
    MORNING_1(LocalTime.of(10, 0), LocalTime.of(11, 0)),
    MORNING_2(LocalTime.of(11, 0), LocalTime.of(12, 0)),
    AFTERNOON_1(LocalTime.of(13, 0), LocalTime.of(14, 0)),
    AFTERNOON_2(LocalTime.of(14, 0), LocalTime.of(15, 0)),
    AFTERNOON_3(LocalTime.of(16, 0), LocalTime.of(17, 0));

    private final LocalTime startTime;
    private final LocalTime endTime;
}
