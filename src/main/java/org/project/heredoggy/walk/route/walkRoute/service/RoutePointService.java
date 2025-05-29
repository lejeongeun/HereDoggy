package org.project.heredoggy.walk.route.walkRoute.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.walk.route.RoutePointRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutePointService {
    private final RoutePointRepository routePointRepository;

}
