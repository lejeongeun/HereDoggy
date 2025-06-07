package org.project.heredoggy.systemAdmin.inquiry.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.inquiry.dto.AdminInquiryAnswerDTO;
import org.project.heredoggy.systemAdmin.inquiry.dto.AdminInquiryDetailResponseDTO;
import org.project.heredoggy.systemAdmin.inquiry.dto.AdminInquiryResponseDTO;
import org.project.heredoggy.systemAdmin.inquiry.service.AdminInquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/inquiry")
public class AdminInquiryController {
    private final AdminInquiryService adminInquiryService;

    //문의 내역 리스트 조회
    @GetMapping
    public ResponseEntity<List<AdminInquiryResponseDTO>> getAdminInquiries(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<AdminInquiryResponseDTO> lists = adminInquiryService.getAdminInquiries(userDetails);
        return ResponseEntity.ok(lists);
    }

    //문의 내역 상세 조회
    @GetMapping("/{inquiry_id}")
    public ResponseEntity<AdminInquiryDetailResponseDTO> getAdminInquiryDetail(@PathVariable("inquiry_id") Long inquiryId,
                                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        AdminInquiryDetailResponseDTO res = adminInquiryService.getAdminInquiryDetail(inquiryId, userDetails);
        return ResponseEntity.ok(res);
    }

    //문의 답변
    @PatchMapping("/{inquiry_id}/answer")
    public ResponseEntity<Map<String, String>> inquiryAdminAnswer(@PathVariable("inquiry_id") Long inquiryId,
                                                             @RequestBody AdminInquiryAnswerDTO request,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminInquiryService.inquiryAdminAnswer(inquiryId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "답변 완료"));

    }
}
