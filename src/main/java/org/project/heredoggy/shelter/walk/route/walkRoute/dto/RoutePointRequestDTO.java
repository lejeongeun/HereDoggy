package org.project.heredoggy.shelter.walk.route.walkRoute.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.walk.walkRoute.PointType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutePointRequestDTO {
    private Double lat;
    private Double lng;
    private int sequence;
    private PointType pointType;
}
