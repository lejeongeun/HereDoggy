package org.project.heredoggy.walk.route.walkRoute.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.walk.route.RoutePointRepository;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutePointService {
    private final RoutePointRepository routePointRepository;

    public void createRoutePoint(CustomUserDetails userDetails, Long sheltersId, Long walkRoutesId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);


    }
}
