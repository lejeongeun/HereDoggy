package org.project.heredoggy.user.walk.walkRecord.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class WalkSimpleStatisticDTO {
    private Double totalDistance;
    private Integer totalDuration;
    private Long totalWalkCount;

    public WalkSimpleStatisticDTO(Double totalDistance, Integer totalDuration, Long totalWalkCount) {
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.totalWalkCount = totalWalkCount;
    }
}
