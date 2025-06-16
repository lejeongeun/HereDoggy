package org.project.heredoggy.shelter.walk.walkRoute.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalkRouteResponseDTO {
    private Long id;
    private String routeName;
    private String description;
    private Double totalDistance;
    private Integer expectedDuration;
    private LocalDateTime createdAt;
    private List<RoutePointResponseDTO> points;
    private String thumbnailUrl;
}