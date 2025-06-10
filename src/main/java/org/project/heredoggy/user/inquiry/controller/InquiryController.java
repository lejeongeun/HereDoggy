package org.project.heredoggy.user.inquiry.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.inquiry.dto.InquiryMyResponseDTO;
import org.project.heredoggy.user.inquiry.dto.InquiryRequestDTO;
import org.project.heredoggy.user.inquiry.service.InquiryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InquiryController {
    private final InquiryService inquiryService;
    private final ObjectMapper objectMapper;

    //문의 등록하기
    @PostMapping(value = "/inquiry",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> registerInquires(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("info") String infoJson,
            @RequestPart("images") List<MultipartFile> images) {
        try {
            InquiryRequestDTO request = objectMapper.readValue(infoJson, InquiryRequestDTO.class);
            inquiryService.registerInquires(userDetails, request, images);
            return ResponseEntity.ok(Map.of("message", "문의 등록 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "서버에러", "error", e.getMessage()));
        } catch (ErrorResponseException e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버에러", "error", e.getMessage()));
        }
    }


    //나의 문의 리스트 조회
    @GetMapping("/me/inquiry")
    public ResponseEntity<List<InquiryMyResponseDTO>> getAllMyInquires(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<InquiryMyResponseDTO> res = inquiryService.getAllMyInquires(userDetails);
        return ResponseEntity.ok(res);
    }

    //나의 문의 세부내용 조회
    @GetMapping("/me/inquiry/{inquiry_id}")
    public ResponseEntity<InquiryMyResponseDTO> getMyInquire(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable("inquiry_id") Long inquiryId) {
        InquiryMyResponseDTO res = inquiryService.getMyInquires(userDetails, inquiryId);
        return ResponseEntity.ok(res);
    }

    //본의 문의 내역 지우기(db는 안지워짐)
    @PostMapping("/me/inquiry/{inquiry_id}")
    public ResponseEntity<Map<String, String>> deleteMyInquire(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable("inquiry_id") Long inquiryId) {
        inquiryService.deleteInquires(userDetails, inquiryId);
        return ResponseEntity.ok(Map.of("message", "삭제 완료"));
    }
}
