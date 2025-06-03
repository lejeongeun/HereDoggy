package org.project.heredoggy.user.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.adoption.dto.MemberAdoptionResponseDTO;
import org.project.heredoggy.user.adoption.service.MemberAdoptionService;
import org.project.heredoggy.user.fcm.service.FcmTokenService;
import org.project.heredoggy.user.member.dto.request.MemberEditRequestDTO;
import org.project.heredoggy.user.member.dto.response.MemberDetailResponseDTO;
import org.project.heredoggy.user.member.dto.response.MyPostResponseDTO;
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
    private final FcmTokenService fcmTokenService;
    private final MemberAdoptionService adoptionService;

    // ==============================
    //    👤 내 정보 CRUD
    // ==============================
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
        fcmTokenService.deleteByMember(userDetails.getMember());
        memberService.remove(userDetails);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴 성공"));
    }

    // ==============================
    //    📅 산책 예약
    // ==============================
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

    // ==============================
    //    🐕 입양
    // ==============================
    @GetMapping("/adoptions")
    public ResponseEntity<List<MemberAdoptionResponseDTO>> getAllMyAdoptions(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<MemberAdoptionResponseDTO> adoptionsList = adoptionService.getAllMyAdoptions(userDetails);
        return ResponseEntity.ok(adoptionsList);
    }

    @GetMapping("/adoptions/{adoptions_id}")
    public ResponseEntity<MemberAdoptionResponseDTO> getDetailsMyAdoptions(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @PathVariable("adoptions_id") Long adoptionsId){
        MemberAdoptionResponseDTO adoptionsDetails = adoptionService.getDetailsMyAdoptions(userDetails, adoptionsId);
        return ResponseEntity.ok(adoptionsDetails);
    }

    // ==============================
    //    🔔 알림 설정
    // ==============================
    @PatchMapping("/me/notification-enabled")
    public ResponseEntity<Map<String,String>> updateNotificationSetting(@RequestParam boolean enabled,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updateNotificationSetting(userDetails, enabled);
        return ResponseEntity.ok(Map.of("message", enabled ? "알림 수신 설정: ON" : "알림 수신 설정: OFF"));
    }

    // ==============================
    //    🗒️ 게시물 조회
    // ==============================

    @GetMapping("/me/posts")
    public ResponseEntity<MyPostResponseDTO> getMyPostList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MyPostResponseDTO res = memberService.getMyFreePostList(userDetails);
        return ResponseEntity.ok(res);
    }
}
