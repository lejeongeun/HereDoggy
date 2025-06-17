package org.project.heredoggy.user.walk.walkRecord.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecordStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecordResponseDTO {
    private Long id;
    private Long reservationId;
    private Long walkRouteId;
    private Double actualDistance;
    private Integer actualDuration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<WalkRecordPointDTO> actualPath;
    private WalkRecordStatus status;
    private String thumbnailUrl;
}
