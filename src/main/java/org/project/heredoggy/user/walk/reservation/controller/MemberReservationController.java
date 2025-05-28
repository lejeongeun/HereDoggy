package org.project.heredoggy.user.walk.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationRequestDTO;
import org.project.heredoggy.user.walk.reservation.service.MemberReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class MemberReservationController {
    private final MemberReservationService memberReservationService;
    @PostMapping
    public ResponseEntity<Map<String, String>> requestReservation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @Valid @RequestBody MemberReservationRequestDTO requestDTO){
        memberReservationService.requestReservation(userDetails, requestDTO);
        return ResponseEntity.ok(Map.of("message", "산책 예약이 신청되었습니다."));
    }
}