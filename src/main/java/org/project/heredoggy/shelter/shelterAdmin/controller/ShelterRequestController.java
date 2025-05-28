package org.project.heredoggy.shelter.shelterAdmin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.shelterAdmin.dto.ShelterCreateRequestDTO;
import org.project.heredoggy.shelter.shelterAdmin.service.ShelterRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShelterRequestController {
    private final ShelterRequestService shelterService;

    @PostMapping("/shelterRequest")
    public ResponseEntity<Map<String, String>> requestShelter(@Valid @RequestBody ShelterCreateRequestDTO request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println("userDetails = " + userDetails);
        shelterService.createRequest(request, userDetails);
        return ResponseEntity.ok(Map.of("message", "요청 성공"));
    }
}
