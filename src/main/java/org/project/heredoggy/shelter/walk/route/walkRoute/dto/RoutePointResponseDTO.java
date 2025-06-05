package org.project.heredoggy.shelter.walk.route.walkRoute.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.walk.route.PointType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutePointResponseDTO {
    private Long id;
    private Double lat;
    private Double lng;
    private int sequence;
    private PointType pointType;
}