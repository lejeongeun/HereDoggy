package org.project.heredoggy.shelter.inquiry.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.inquiry.dto.ShelterInquiryAnswerDTO;
import org.project.heredoggy.shelter.inquiry.dto.ShelterInquiryDetailResponseDTO;
import org.project.heredoggy.shelter.inquiry.dto.ShelterInquiryResponseDTO;
import org.project.heredoggy.shelter.inquiry.service.ShelterInquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/{shelter_id}/inquiry")
public class ShelterInquiryController {
    private final ShelterInquiryService shelterInquiryService;

    //문의 내역 리스트 조회
    @GetMapping
    public ResponseEntity<List<ShelterInquiryResponseDTO>> getShelterInquiries(@PathVariable("shelter_id") Long shelterId,
                                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ShelterInquiryResponseDTO> lists = shelterInquiryService.getShelterInquiries(shelterId, userDetails);
        return ResponseEntity.ok(lists);
    }

    //문의 내역 상세 조회
    @GetMapping("/{inquiry_id}")
    public ResponseEntity<ShelterInquiryDetailResponseDTO> getShelterInquiryDetail(@PathVariable("shelter_id") Long shelterId,
                                                                                   @PathVariable("inquiry_id") Long inquiryId,
                                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        ShelterInquiryDetailResponseDTO res = shelterInquiryService.getShelterInquiryDetail(shelterId, inquiryId, userDetails);
        return ResponseEntity.ok(res);
    }

    //문의 답변
    @PatchMapping("/{inquiry_id}/answer")
    public ResponseEntity<Map<String, String>> inquiryAnswer(@PathVariable("shelter_id") Long shelterId,
                                                             @PathVariable("inquiry_id") Long inquiryId,
                                                             @RequestBody ShelterInquiryAnswerDTO request,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        shelterInquiryService.inquiryAnswer(shelterId, inquiryId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "답변 완료"));

    }
}
