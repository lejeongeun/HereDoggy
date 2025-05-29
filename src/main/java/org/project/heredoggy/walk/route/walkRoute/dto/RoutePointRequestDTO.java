package org.project.heredoggy.walk.route.walkRoute.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutePointRequestDTO {
    private Double lat;
    private Double lng;
    private int sequence;
}
