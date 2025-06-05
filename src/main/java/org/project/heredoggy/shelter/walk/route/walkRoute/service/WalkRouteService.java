package org.project.heredoggy.shelter.walk.route.walkRoute.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.walk.route.RoutePoint;
import org.project.heredoggy.domain.postgresql.walk.route.WalkRouteRepository;
import org.project.heredoggy.domain.postgresql.walk.route.WalkRoute;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.route.walkRoute.dto.WalkRouteRequestDto;
import org.project.heredoggy.shelter.walk.route.walkRoute.dto.WalkRouteResponseDTO;
import org.project.heredoggy.shelter.walk.route.walkRoute.mapper.WalkRouteMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalkRouteService {
    private final WalkRouteRepository walkRouteRepository;
    private final WalkRouteMapper walkRouteMapper;

    public void createRoute(CustomUserDetails userDetails, Long sheltersId, WalkRouteRequestDto request) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        long count = walkRouteRepository.countByShelterId(sheltersId);

        if (count >= 3){
            throw new BadRequestException(ErrorMessages.WALK_ROUTE_LIMIT);
        }
        // 경로 제공 컴포넌트 제공
        WalkRoute walkRoute = WalkRoute.builder()
                .routeName(request.getRouteName())
                .description(request.getDescription())
                .totalDistance(request.getTotalDistance())
                .expectedDuration(request.getExpectedDuration())
                .shelter(shelter)
                .build();

        request.getPoints().forEach(p -> {
            RoutePoint point = RoutePoint.builder()
                    .lat(p.getLat())
                    .lng(p.getLng())
                    .sequence(p.getSequence())
                    .pointType(p.getPointType())
                    .build();

            walkRoute.addPoint(point);
        });

        walkRouteRepository.save(walkRoute);
    }

    public List<WalkRouteResponseDTO> getAllWalkRoute(CustomUserDetails userDetails, Long sheltersId) {
        SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        return walkRouteRepository.findAllByShelterId(sheltersId).stream()
                .map(walkRouteMapper::toDto)
                .collect(Collectors.toList());
    }

    public WalkRouteResponseDTO getDetailsWalkRoute(CustomUserDetails userDetails, Long sheltersId, Long walkRoutesId) {
        SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        WalkRoute walkRoute = walkRouteRepository.findById(walkRoutesId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.WALK_ROUTE_NOT_FOUND));
        return walkRouteMapper.toDto(walkRoute);

    }


    public void deleteWalkRoute(CustomUserDetails userDetails, Long sheltersId, Long walkRoutesId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        WalkRoute walkRoute = walkRouteRepository.findById(walkRoutesId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.WALK_ROUTE_NOT_FOUND));
        if (!walkRoute.getShelter().getId().equals(shelter.getId())){
            throw new BadRequestException("해당 보호소의 산책 경로가 아닙니다.");
        }

        walkRouteRepository.delete(walkRoute);
    }
}