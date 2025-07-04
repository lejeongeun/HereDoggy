package org.project.heredoggy.shelter.volunteer.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.volunteer.reservation.service.VolunteerReservationManageService;
import org.project.heredoggy.user.volunteer.dto.VolunteerReservationResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/volunteer/reservations")
public class VolunteerReservationManageController {

    private final VolunteerReservationManageService volunteerReservationManageService;

    // 보호소의 예약 목록 조회
    @GetMapping
    public ResponseEntity<List<VolunteerReservationResponseDTO>> getReservation(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(volunteerReservationManageService.getReservationForShelter(userDetails));
    }

    // 봉사 신청 승인 또는 거절
    @PutMapping("/{reservation_id}/status")
    public ResponseEntity<Map<String, String>> updateStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable("reservation_id") Long reservationId,
                                                            @RequestParam("status") String status) {
        volunteerReservationManageService.updateReservationStatus(userDetails, reservationId, status);
        return ResponseEntity.ok(Map.of("message", "처리 완료"));
    }

    @PutMapping("/{reservation_id}/cancel-approve")
    public ResponseEntity<Map<String, String>> approveCancel(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable("reservation_id") Long reservation_id) {
        volunteerReservationManageService.cancelReservationStatus(userDetails, reservation_id);
        return ResponseEntity.ok(Map.of("message", "처리 완료"));
    }
}