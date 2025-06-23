package org.project.heredoggy.shelter.shelter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.shelter.dto.ShelterProfileResponseDTO;
import org.project.heredoggy.shelter.shelter.dto.SheltersDetailResponseDTO;
import org.project.heredoggy.shelter.shelter.dto.SheltersResponseDTO;
import org.project.heredoggy.shelter.shelter.service.ShelterService;
import org.project.heredoggy.shelter.shelterAdmin.dto.ShelterEditRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters")
public class ShelterController {
    private final ShelterService shelterService;
    private final ObjectMapper objectMapper;

    @GetMapping("/profile")
    public ResponseEntity<ShelterProfileResponseDTO> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        ShelterProfileResponseDTO shelterProfile = shelterService.getMyProfile(userDetails);
        return ResponseEntity.ok(shelterProfile);
    }

    @GetMapping
    public ResponseEntity<List<SheltersResponseDTO>> findShelters(@RequestParam(required = false) String region) {
        List<SheltersResponseDTO> lists = shelterService.getSheltersByRegion(region);
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/{shelter_id}")
    public ResponseEntity<SheltersDetailResponseDTO> getShelterDetail(@PathVariable("shelter_id") Long shelterId) {
        SheltersDetailResponseDTO shelter = shelterService.getShelterDetail(shelterId);
        return ResponseEntity.ok(shelter);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> editShelters(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("info") String infoJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            ShelterEditRequestDTO request = objectMapper.readValue(infoJson, ShelterEditRequestDTO.class);
            shelterService.editShelter(userDetails, request, files);
            return ResponseEntity.ok(Map.of("message", "수정 성공"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 실패", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버 오류", "error", e.getMessage()));
        }
    }

}
