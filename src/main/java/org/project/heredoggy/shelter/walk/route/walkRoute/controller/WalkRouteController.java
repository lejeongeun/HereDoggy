package org.project.heredoggy.shelter.walk.route.walkRoute.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.route.walkRoute.dto.WalkRouteRequestDto;
import org.project.heredoggy.shelter.walk.route.walkRoute.dto.WalkRouteResponseDTO;
import org.project.heredoggy.shelter.walk.route.walkRoute.service.WalkRouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shelters/{shelters_id}/walk-routes")
@RequiredArgsConstructor
public class WalkRouteController {
    private final WalkRouteService walkRouteService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createRoute(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable("shelters_id") Long sheltersId,
                                                           @Valid @RequestBody WalkRouteRequestDto request){
        walkRouteService.createRoute(userDetails, sheltersId, request);
        return ResponseEntity.ok(Map.of("message", "기본 경로가 생성되었습니다."));
    }
    @GetMapping
    public ResponseEntity<List<WalkRouteResponseDTO>> getAllWalkRoute(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable("shelters_id") Long sheltersId){
        List<WalkRouteResponseDTO> walkRouteList = walkRouteService.getAllWalkRoute(userDetails, sheltersId);
        return ResponseEntity.ok(walkRouteList);
    }
    @GetMapping("/{walk-routes_id}")
    public ResponseEntity<WalkRouteResponseDTO> getDetailsWalkRoute(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PathVariable("shelters_id") Long sheltersId,
                                                                    @PathVariable("walk-routes_id") Long walkRoutesId){
        WalkRouteResponseDTO walkRouteDetails = walkRouteService.getDetailsWalkRoute(userDetails, sheltersId, walkRoutesId);
        return ResponseEntity.ok(walkRouteDetails);
    }
    @DeleteMapping("/{walk-routes_id}")
    public ResponseEntity<Map<String, String>> deleteWalkRoute(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable("shelters_id") Long sheltersId,
                                                               @PathVariable("walk-routes_id") Long walkRoutesId){
        walkRouteService.deleteWalkRoute(userDetails, sheltersId, walkRoutesId);
        return ResponseEntity.ok(Map.of("message", "산책 경로 컴포넌트가 삭제되었습니다."));
    }
}