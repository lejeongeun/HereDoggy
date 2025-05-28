package org.project.heredoggy.shelter.walk.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.reservation.dto.ShelterReservationResponseDto;
import org.project.heredoggy.shelter.walk.reservation.service.ShelterReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shelters/{shelters_id}/reservations")
@RequiredArgsConstructor
public class ShelterReservationController {

    private final ShelterReservationService shelterReservationService;

    @GetMapping
    public ResponseEntity<List<ShelterReservationResponseDto>> getAllReservations(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                  @PathVariable("shelters_id") Long sheltersId){

        List<ShelterReservationResponseDto> reservationsList = shelterReservationService.getAllReservations(userDetails, sheltersId);
        return ResponseEntity.ok(reservationsList);
    }

    // 상세 조회
    @GetMapping("/{reservations_id}")
    public ResponseEntity<ShelterReservationResponseDto> getDetailsReservations(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                               @PathVariable("shelters_id") Long sheltersId,
                                                                               @PathVariable("reservations_id") Long reservationsId){
        ShelterReservationResponseDto reservationsList = shelterReservationService.getDetailsReservations(userDetails, sheltersId, reservationsId);
        return ResponseEntity.ok(reservationsList);
    }

    // 상태 변경
    @PutMapping("/{reservations_id}/approve")
    public ResponseEntity<Map<String, String>> approveReservations(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PathVariable("shelters_id") Long sheltersId,
                                                                   @PathVariable("reservations_id") Long reservationsId){
        shelterReservationService.approveReservations(userDetails, sheltersId, reservationsId);
        return ResponseEntity.ok(Map.of("message", "요청이 승인되었습니다."));
    }

    @PutMapping("/{reservations_id}/reject")
    public ResponseEntity<Map<String, String>> rejectReservations(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable("shelters_id") Long sheltersId,
                                                                  @PathVariable("reservations_id") Long reservationsId){
        shelterReservationService.rejectReservations(userDetails, sheltersId, reservationsId);
        return ResponseEntity.ok(Map.of("message", "요청이 거절되었습니다."));
    }

    // 취소 됨 변경
    @PutMapping("/{reservations_id}/cancel")
    public ResponseEntity<Map<String, String>> cancelReservations(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable("shelters_id") Long sheltersId,
                                                                  @PathVariable("reservations_id") Long reservationsId){
        shelterReservationService.cancelReservations(userDetails, sheltersId, reservationsId);
        return ResponseEntity.ok(Map.of("message", "요청이 취소 되었습니다."));
    }
}
