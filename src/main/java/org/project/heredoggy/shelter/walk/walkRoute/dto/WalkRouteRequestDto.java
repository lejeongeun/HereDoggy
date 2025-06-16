package org.project.heredoggy.shelter.walk.walkRoute.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalkRouteRequestDto {
    @NotBlank(message = "코스명을 입력해 주세요")
    private String routeName;
    private String description;
    private Double totalDistance; // 총 거리
    private Integer expectedDuration; // 누적 시간
    private List<RoutePointRequestDTO> points;
}
