package org.project.heredoggy.user.walk.walkRecord.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRecordPointDTO {
    private Double latitude;
    private Double longitude;
    private LocalDateTime recordedAt;

}
