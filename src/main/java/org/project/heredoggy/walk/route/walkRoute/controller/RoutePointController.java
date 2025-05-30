package org.project.heredoggy.walk.route.walkRoute.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.walk.route.walkRoute.service.RoutePointService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/shelters/{shelters_id}/walk-routes/{walk-routes_id}/route-point")
@RequiredArgsConstructor
public class RoutePointController {
    private final RoutePointService routePointService;

    public ResponseEntity<Map<String, String>> createRoutePoint(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable("shelters_id") Long sheltersId,
                                                                @PathVariable("walk-route_id") Long walkRoutesId){

        routePointService.createRoutePoint(userDetails, sheltersId, walkRoutesId);
        return ResponseEntity.ok(Map.of("messasge", "세부 경로가 추가되었습니다."));
    }

}
