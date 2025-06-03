package org.project.heredoggy.user.adoption.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.adoption.dto.MemberAdoptionRequestDTO;
import org.project.heredoggy.user.adoption.service.MemberAdoptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adoptions/dogs/{dogs_id}")
public class MemberAdoptionController {
    private final MemberAdoptionService adoptionService;

    @PostMapping
    public ResponseEntity<Map<String, String>> requestAdoption(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable("dogs_id") Long dogsId,
                                                               @Valid @RequestBody MemberAdoptionRequestDTO request){
        adoptionService.requestAdoption(userDetails, dogsId, request);
        return ResponseEntity.ok(Map.of("message", "입양 신청이 등록되었습니다."));
    }

}
