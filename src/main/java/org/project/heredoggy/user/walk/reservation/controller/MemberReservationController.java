package org.project.heredoggy.user.walk.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationRequestDTO;
import org.project.heredoggy.user.walk.reservation.service.MemberReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations/dogs")
public class MemberReservationController {

    private final MemberReservationService memberReservationService;

    @GetMapping
    public ResponseEntity<List<DogResponseDTO>> getAllReservationsDog(){
        List<DogResponseDTO> dogResponseList = memberReservationService.getAllReservationDog();
        return ResponseEntity.ok(dogResponseList);
    }
    @GetMapping("/{dogs_id}")
    public ResponseEntity<DogResponseDTO> getDetailsReservationsDog(@PathVariable("dogs_id") Long dogsId){
        DogResponseDTO dogDetails = memberReservationService.getDetailsReservationsDog(dogsId);
        return ResponseEntity.ok(dogDetails);
    }

    @GetMapping("/{dogs_id}/unavailable-dates")
    public ResponseEntity<List<LocalDate>> getUnavailableList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PathVariable("dogs_id") Long dogsId) {
        List<LocalDate> dates = memberReservationService.getUnavailableList(userDetails, dogsId);
        return ResponseEntity.ok(dates);
    }

    @PostMapping("/{dogs_id}/reservationsRequest")
    public ResponseEntity<Map<String, String>> requestReservation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable("dogs_id") Long dogsId,
                                                                  @Valid @RequestBody MemberReservationRequestDTO requestDTO) {
        memberReservationService.requestReservation(userDetails, dogsId, requestDTO);
        return ResponseEntity.ok(Map.of("message", "산책 예약이 신청되었습니다."));
    }
}