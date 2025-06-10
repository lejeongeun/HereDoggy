package org.project.heredoggy.user.walk.walkRecord.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecordPointDTO {
    private Long id;
    private Long reservationId;
    private Long walkRouteId;
    private Double actualDistance;
    private Integer actualDuration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<WalkRecordPointDTO> actualPath;
    private String status;
}
