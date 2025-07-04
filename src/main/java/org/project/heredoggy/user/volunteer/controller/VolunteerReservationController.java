package org.project.heredoggy.user.volunteer.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.volunteer.dto.VolunteerReservationRequestDTO;
import org.project.heredoggy.user.volunteer.dto.VolunteerReservationResponseDTO;
import org.project.heredoggy.user.volunteer.dto.VolunteerUnavailableTimeDTO;
import org.project.heredoggy.user.volunteer.service.VolunteerReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/volunteer")
public class VolunteerReservationController {
    private final VolunteerReservationService volunteerReservationService;


    //봉사 신청
    @PostMapping
    public ResponseEntity<Map<String, String>> reserve(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody VolunteerReservationRequestDTO requestDTO) {

        if(requestDTO.getIsGroup()) {
            return ResponseEntity.badRequest().body(Map.of("message", "단체 봉사는 보호소에 별로로 문의부탁드립니다."));
        }

        volunteerReservationService.reserve(userDetails, requestDTO);
        return ResponseEntity.ok(Map.of("message", "예약이 완료 되었습니다."));
    }

    //PENDING 상태일때 봉사 신청 취소하기
    @DeleteMapping("/reservations/{reservation_id}")
    public ResponseEntity<Map<String, String>> requestPendingCancel(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PathVariable("reservation_id") Long reservationId) {
        volunteerReservationService.cancelPendingReservation(userDetails, reservationId);
        return ResponseEntity.ok(Map.of("message", "취소가 완료되었습니다."));
    }

    //ACCEPTED 상태일때 봉사 신청 취소 요청하기
    @PostMapping("/reservations/{id}/cancel-request")
    public ResponseEntity<Map<String, String>> requestCancel(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable Long id) {
        volunteerReservationService.requestCancel(userDetails, id);
        return ResponseEntity.ok(Map.of("message", "취소 요청이 접수되었습니다."));
    }

    //내 예약 내역 조회
    @GetMapping("/me")
    public ResponseEntity<List<VolunteerReservationResponseDTO>> myReservation(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(volunteerReservationService.getMyReservations(userDetails));
    }


    //예약 불가능 시간대 조회(사용자가)
    @GetMapping("/{shelter_id}/unavailable")
    public ResponseEntity<List<VolunteerUnavailableTimeDTO>> getUnavailableTimes(@PathVariable("shelter_id") Long shelterId,
                                                                                 @RequestParam LocalDate date) {
        List<VolunteerUnavailableTimeDTO> res = volunteerReservationService.getUnavailableTimes(shelterId, date);
        return ResponseEntity.ok(res);
    }
}
