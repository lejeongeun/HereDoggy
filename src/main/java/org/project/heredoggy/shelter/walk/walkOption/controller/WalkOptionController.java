package org.project.heredoggy.shelter.walk.walkOption.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.walkOption.dto.WalkOptionRequestDTO;
import org.project.heredoggy.shelter.walk.walkOption.service.WalkOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/{shelters_id}/dogs/{dogs_id}/walk-options")
public class WalkOptionController {
    private final WalkOptionService walkOptionService;

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable("shelters_id") Long sheltersId,
                                                      @PathVariable("dogs_id") Long dogsId,
                                                      @Valid @RequestBody WalkOptionRequestDTO request){
        walkOptionService.create(userDetails, sheltersId, dogsId, request);
        return ResponseEntity.ok(Map.of("message", "예약 옵션이 생성되었습니다."));
    }
    @PutMapping("/{options_id}")
    public ResponseEntity<Map<String, String>> edit(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable("shelters_id") Long sheltersId,
                                                    @PathVariable("dogs_id") Long dogsId,
                                                    @PathVariable("options_id") Long optionsId,
                                                    @Valid @RequestBody WalkOptionRequestDTO request){
        walkOptionService.edit(userDetails, sheltersId, dogsId, request, optionsId);
        return ResponseEntity.ok(Map.of("message", "예약 옵션이 수정되었습니다."));
    }
    @DeleteMapping("/{options_id}")
    public ResponseEntity<Map<String, String>> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @PathVariable("shelters_id") Long sheltersId,
                                                      @PathVariable("dogs_id") Long dogsId,
                                                      @PathVariable("options_id") Long optionsId){
        walkOptionService.delete(userDetails, sheltersId, dogsId, optionsId);
        return ResponseEntity.ok(Map.of("message", "예약 옵션이 삭제되었습니다."));

    }



}
