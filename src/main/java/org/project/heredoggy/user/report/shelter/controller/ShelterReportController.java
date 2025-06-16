package org.project.heredoggy.user.report.shelter.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;

import org.project.heredoggy.user.report.shelter.dto.ShelterReportRequestDTO;
import org.project.heredoggy.user.report.shelter.service.ShelterReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/shelters")
public class ShelterReportController {
    private final ShelterReportService shelterReportService;

    @PostMapping
    public ResponseEntity<?> report(@RequestBody ShelterReportRequestDTO request,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        shelterReportService.reportShelter(userDetails, request);
        return ResponseEntity.ok(Map.of("message", "보호소 신고가 접수되었습니다."));
    }
}
