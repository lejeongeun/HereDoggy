package org.project.heredoggy.shelter.walk.route.walkRoute.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutePointResponseDTO {
    private Long id;
    private Long walkRouteId;
    private Double lat;
    private Double lng;
    private int sequence;
}