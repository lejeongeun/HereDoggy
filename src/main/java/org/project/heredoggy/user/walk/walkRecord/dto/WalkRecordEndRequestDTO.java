package org.project.heredoggy.user.walk.walkRecord.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecordEndRequestDTO {
    private Double actualDistance;
    private Integer actualDuration;
    private List<WalkRecordPointDTO> actualPath;
}
