package org.project.heredoggy.user.walk.walkRecord.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecordStartRequestDTO {
    private Long reservationId;
    private Long walkRouteId;

}
