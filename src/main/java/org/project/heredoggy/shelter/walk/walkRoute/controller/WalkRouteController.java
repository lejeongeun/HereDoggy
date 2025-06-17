package org.project.heredoggy.shelter.walk.walkRoute.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.walkRoute.dto.WalkRouteRequestDto;
import org.project.heredoggy.shelter.walk.walkRoute.dto.WalkRouteResponseDTO;
import org.project.heredoggy.shelter.walk.walkRoute.service.WalkRouteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shelters/{shelters_id}/walk-routes")
@RequiredArgsConstructor
public class WalkRouteController {
    private final WalkRouteService walkRouteService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createRoute(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable("shelters_id") Long sheltersId,
                                                           @RequestPart("request") String requestJson,
                                                           @RequestPart("image") MultipartFile image){
        WalkRouteRequestDto requestDto;
        try{
            requestDto = objectMapper.readValue(requestJson, WalkRouteRequestDto.class);
        } catch (JsonProcessingException e){
            throw new BadRequestException("JSON 파싱에 실패하셨습니다.");
        }

        walkRouteService.createRoute(userDetails, sheltersId, requestDto, image);
        return ResponseEntity.ok(Map.of("message", "기본 경로가 생성되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<WalkRouteResponseDTO>> getAllWalkRoute(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable("shelters_id") Long sheltersId){
        List<WalkRouteResponseDTO> walkRouteList = walkRouteService.getAllWalkRoute(userDetails, sheltersId);
        return ResponseEntity.ok(walkRouteList);
    }

    @GetMapping("/{walk_routes_id}")
    public ResponseEntity<WalkRouteResponseDTO> getDetailsWalkRoute(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PathVariable("shelters_id") Long sheltersId,
                                                                    @PathVariable("walk_routes_id") Long walkRoutesId){
        WalkRouteResponseDTO walkRouteDetails = walkRouteService.getDetailsWalkRoute(userDetails, sheltersId, walkRoutesId);
        return ResponseEntity.ok(walkRouteDetails);
    }

    @DeleteMapping("/{walk_routes_id}")
    public ResponseEntity<Map<String, String>> deleteWalkRoute(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable("shelters_id") Long sheltersId,
                                                               @PathVariable("walk_routes_id") Long walkRoutesId){
        walkRouteService.deleteWalkRoute(userDetails, sheltersId, walkRoutesId);
        return ResponseEntity.ok(Map.of("message", "산책 경로 컴포넌트가 삭제되었습니다."));
    }
}