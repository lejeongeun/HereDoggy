package org.project.heredoggy.shelter.volunteer.unavailableDate.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.volunteer.VolunteerReservation;
import org.project.heredoggy.domain.postgresql.volunteer.VolunteerReservationRepository;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.volunteer.unavailableDate.dto.VolunteerUnavailableRequestDTO;
import org.project.heredoggy.shelter.volunteer.unavailableDate.dto.VolunteerUnavailableResponseDTO;
import org.project.heredoggy.shelter.volunteer.unavailableDate.service.VolunteerUnavailableService;
import org.project.heredoggy.user.volunteer.dto.VolunteerReservationResponseDTO;
import org.project.heredoggy.user.volunteer.service.VolunteerReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/volunteer/unavailable")
public class VolunteerUnavailableController {
    private final VolunteerUnavailableService volunteerUnavailableService;
    private final VolunteerReservationService volunteerReservationService;

    // 각 보호소 불가능 시간대 설정
    @PostMapping
    public ResponseEntity<Map<String, String>> createUnavailableTime(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @RequestBody VolunteerUnavailableRequestDTO requestDTO) {
        volunteerUnavailableService.create(userDetails, requestDTO);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    // 불가능 시간대 리스트 조회
    @GetMapping
    public ResponseEntity<List<VolunteerUnavailableResponseDTO>> getList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<VolunteerUnavailableResponseDTO> res = volunteerUnavailableService.getAllForCurrentShelter(userDetails);
        return ResponseEntity.ok(res);
    }


    //불가능 시간대 삭제
    @DeleteMapping("/{unavailable_id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("unavailable_id") Long unavailableId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        volunteerUnavailableService.delete(userDetails, unavailableId);
        return ResponseEntity.ok(Map.of("message", "삭제 완료"));
    }

}
