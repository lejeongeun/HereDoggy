package org.project.heredoggy.shelter.adoption.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.adoption.dto.AdoptionDetailsResponseDto;
import org.project.heredoggy.shelter.adoption.dto.AdoptionListResponseDTO;
import org.project.heredoggy.shelter.adoption.service.ShelterAdoptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/{shelters_id}/adoptions")
public class ShelterAdoptionController {

    private final ShelterAdoptionService shelterAdoptionService;

    @GetMapping
    public ResponseEntity<List<AdoptionListResponseDTO>> getAllAdoptions(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @PathVariable("shelters_id") Long sheltersId){
        List<AdoptionListResponseDTO> allAdoptions = shelterAdoptionService.getAllAdoptions(userDetails, sheltersId);
        return ResponseEntity.ok(allAdoptions);
    }

    @GetMapping("/{adoptions_id}")
    public ResponseEntity<AdoptionDetailsResponseDto> getDetailsAdoptions(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                             @PathVariable("shelters_id") Long sheltersId,
                                                                             @PathVariable("adoptions_id") Long adoptionsId){
        AdoptionDetailsResponseDto adoptionDetails = shelterAdoptionService.getDetailsAdoptions(userDetails, sheltersId, adoptionsId);
        return ResponseEntity.ok(adoptionDetails);
    }

    @PutMapping("/{adoptions_id}/approve")
    public ResponseEntity<Map<String, String>> approveAdoptions(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("shelters_id") Long sheltersId,
                                                                 @PathVariable("adoptions_id") Long adoptionsId){
        shelterAdoptionService.approvedAdoptions(userDetails, sheltersId, adoptionsId);
        return ResponseEntity.ok(Map.of("message", "입양 신청을 승인하셨습니다."));
    }
    @PutMapping("/{adoptions_id}/reject")
    public ResponseEntity<Map<String, String>> rejectAdoptions(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable("shelters_id") Long sheltersId,
                                                               @PathVariable("adoptions_id") Long adoptionsId){
        shelterAdoptionService.rejectAdoptions(userDetails, sheltersId, adoptionsId);
        return ResponseEntity.ok(Map.of("message", "입양 신청을 거절하셨습니다."));
    }
}
