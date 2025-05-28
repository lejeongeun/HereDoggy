package org.project.heredoggy.user.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.member.dto.request.MemberEditRequestDTO;
import org.project.heredoggy.user.member.dto.response.MemberDetailResponseDTO;
import org.project.heredoggy.user.member.service.MemberService;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationResponseDTO;
import org.project.heredoggy.user.walk.reservation.service.MemberReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberReservationService memberReservationService;

    @GetMapping("/profile")
    public ResponseEntity<MemberDetailResponseDTO> getMyDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberDetailResponseDTO member = memberService.getMemberDetails(userDetails);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/edit")
    public ResponseEntity<Map<String, String>> edit(@Valid @RequestBody MemberEditRequestDTO request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.edit(request, userDetails);
        return ResponseEntity.ok(Map.of("message","회원 수정 성공"));
    }

    @DeleteMapping("/removal")
    public ResponseEntity<Map<String, String>> remove(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.remove(userDetails);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴 성공"));
    }
    // 산책 에약
    @GetMapping("/reservations")
    public ResponseEntity<List<MemberReservationResponseDTO>> getAllReservation(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<MemberReservationResponseDTO> reservationList = memberReservationService.getAllReservation(userDetails);
        return ResponseEntity.ok(reservationList);
    }
    @GetMapping("/reservations/{reservations_id}")
    public ResponseEntity<MemberReservationResponseDTO> getDetailsReservation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                              @PathVariable("reservations_id") Long reservationsId){
        MemberReservationResponseDTO reservationDetails = memberReservationService.getDetailsReservation(userDetails, reservationsId);
        return ResponseEntity.ok(reservationDetails);

    }
    @PutMapping("/reservations/{reservations_id}/cancel-request")
    public ResponseEntity<Map<String,String>> cancelRequestReservation(@PathVariable("reservations_id") Long reservationsId,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails){
        memberReservationService.cancelRequestReservation(userDetails, reservationsId);
        return ResponseEntity.ok(Map.of("message", "예약 취소 요청이 전송되었습니다."));
    }

}
