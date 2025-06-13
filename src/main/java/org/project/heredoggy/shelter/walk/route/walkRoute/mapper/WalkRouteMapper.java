package org.project.heredoggy.shelter.walk.route.walkRoute.mapper;

import org.project.heredoggy.domain.postgresql.walk.walkRoute.RoutePoint;
import org.project.heredoggy.domain.postgresql.walk.walkRoute.WalkRoute;
import org.project.heredoggy.shelter.walk.route.walkRoute.dto.RoutePointResponseDTO;
import org.project.heredoggy.shelter.walk.route.walkRoute.dto.WalkRouteResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WalkRouteMapper {
    public WalkRouteResponseDTO toDto(WalkRoute walkRoute){
        return WalkRouteResponseDTO.builder()
                .id(walkRoute.getId())
                .routeName(walkRoute.getRouteName())
                .description(walkRoute.getDescription())
                .totalDistance(walkRoute.getTotalDistance())
                .expectedDuration(walkRoute.getExpectedDuration())
                .createdAt(walkRoute.getCreatedAt())
                .points(toPointDtoList(walkRoute.getPoints()))
                .build();
    }

    public List<RoutePointResponseDTO> toPointDtoList(List<RoutePoint> points){
        return points.stream()
                .map(p -> RoutePointResponseDTO.builder()
                        .id(p.getId())
                        .lat(p.getLat())
                        .lng(p.getLng())
                        .sequence(p.getSequence())
                        .pointType(p.getPointType())
                        .build())
                .collect(Collectors.toList());
    }
}
