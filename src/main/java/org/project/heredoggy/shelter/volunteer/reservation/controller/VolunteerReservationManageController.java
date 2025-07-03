package org.project.heredoggy.shelter.volunteer.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.volunteer.dto.VolunteerReservationResponseDTO;
import org.project.heredoggy.user.volunteer.service.VolunteerReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/volunteer/reservations")
public class VolunteerReservationManageController {

    private final VolunteerReservationService volunteerReservationService;

    // 보호소의 예약 목록 조회
    @GetMapping
    public ResponseEntity<List<VolunteerReservationResponseDTO>> getReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(volunteerReservationService.getReservationForShelter(userDetails));
    }

    // 봉사 신청 승인 또는 거절
    @PutMapping("/{reservation_id}/status")
    public ResponseEntity<Map<String, String>> updateStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("reservation_id") Long reservationId,
            @RequestParam("status") String status) {
        volunteerReservationService.updateReservationStatus(userDetails, reservationId, status);
        return ResponseEntity.ok(Map.of("message", "처리 완료"));
    }
}