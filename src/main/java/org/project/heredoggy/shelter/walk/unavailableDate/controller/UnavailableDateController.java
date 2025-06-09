package org.project.heredoggy.shelter.walk.unavailableDate.controller;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.unavailableDate.dto.UnavailableDateRequestDTO;
import org.project.heredoggy.shelter.walk.unavailableDate.service.UnavailableDateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/{shelters_id}/dogs/{dogs_id}/unavailable-dates")
public class UnavailableDateController {

    private final UnavailableDateService unavailableDateService;

    // 예약 불가 등록
    @PostMapping
    public ResponseEntity<Map<String, String>> registerUnavailableDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @PathVariable("shelters_id") Long sheltersId,
                                                                       @PathVariable("dogs_id") Long dogsId,
                                                                       @RequestBody UnavailableDateRequestDTO request){
        unavailableDateService.registerUnavailableDate(userDetails, sheltersId, dogsId, request);
        return ResponseEntity.ok(Map.of("message", "예약 불가 날짜가 설정되었습니다."));

    }

    @GetMapping
    public ResponseEntity<List<LocalDate>> getAllUnavailableDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("shelters_id") Long sheltersId,
                                                                 @PathVariable("dogs_id") Long dogsId){
        List<LocalDate> allDates = unavailableDateService.getAllUnavailableDate(userDetails, sheltersId, dogsId);
        return ResponseEntity.ok(allDates);
    }

    @DeleteMapping("/{unavailable_id}")
    public ResponseEntity<Map<String, String>> removeUnavailableDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @PathVariable("shelters_id") Long sheltersId,
                                                                     @PathVariable("dogs_id") Long dogsId,
                                                                     @PathVariable("unavailable_id") Long unavailableId){
        unavailableDateService.removeUnavailableDate(userDetails, sheltersId, dogsId, unavailableId);
        return ResponseEntity.ok(Map.of("message", "예약 불가 날짜가 취소되었습니다."));

    }



}