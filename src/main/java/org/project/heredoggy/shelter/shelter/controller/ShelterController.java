package org.project.heredoggy.shelter.shelter.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.shelter.shelter.dto.SheltersDetailResponseDTO;
import org.project.heredoggy.shelter.shelter.dto.SheltersResponseDTO;
import org.project.heredoggy.shelter.shelter.service.ShelterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters")
public class ShelterController {
    private final ShelterService shelterService;

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

}
