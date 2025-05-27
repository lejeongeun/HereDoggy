package org.project.heredoggy.systemAdmin.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.dto.ShelterRequestResponseDTO;
import org.project.heredoggy.systemAdmin.service.AdminShelterRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/shelter-requests")
public class AdminShelterRequestController {
    private final AdminShelterRequestService requestService;

    @GetMapping
    public ResponseEntity<List<ShelterRequestResponseDTO>> getAllRequest(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ShelterRequestResponseDTO> res = requestService.getAllRequest(userDetails);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/response")
    public ResponseEntity<List<ShelterRequestResponseDTO>> getAllRequestResponse(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ShelterRequestResponseDTO> res = requestService.getAllRequestResponse(userDetails);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{request_id}/approve")
    public ResponseEntity<Map<String, String>> approveRequest(@PathVariable("request_id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        requestService.approveRequest(id, userDetails);
        return ResponseEntity.ok(Map.of("message","보호소 생성 요청이 승인 되었습니다."));
    }

    @PostMapping("/{request_id}/reject")
    public ResponseEntity<Map<String, String>> rejectRequest(@PathVariable("request_id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        requestService.rejectRequest(id, userDetails);
        return ResponseEntity.ok(Map.of("message","보호소 생성 요청이 거절 되었습니다."));
    }
}
