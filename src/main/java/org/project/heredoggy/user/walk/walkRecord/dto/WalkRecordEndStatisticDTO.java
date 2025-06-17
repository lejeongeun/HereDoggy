package org.project.heredoggy.user.walk.walkRecord.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecordEndStatisticDTO {
    private Double actualDistance;
    private Integer actualDuration;
    private String thumbnailUrl;
}
