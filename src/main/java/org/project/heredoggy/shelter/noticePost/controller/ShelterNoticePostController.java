package org.project.heredoggy.shelter.noticePost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostEditRequestDTO;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostRequestDTO;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostResponseDTO;
import org.project.heredoggy.shelter.noticePost.service.ShelterNoticePostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/notice-posts")
public class ShelterNoticePostController {
    private final ShelterNoticePostService noticePostService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createNoticePost(
            @RequestPart("info") String infoJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            ShelterNoticePostRequestDTO request = objectMapper.readValue(infoJson, ShelterNoticePostRequestDTO.class);
            noticePostService.createNoticePost(request, userDetails, images);
            return ResponseEntity.ok(Map.of("message", "보호소 공지게시판 생성 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 오류", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버오류", "error", e.getMessage()));
        }

    }

    @PutMapping(value = "/{post_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> editNoticePost(
            @PathVariable("post_id") Long postId,
            @RequestPart("info") String infoJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            ShelterNoticePostEditRequestDTO request = objectMapper.readValue(infoJson, ShelterNoticePostEditRequestDTO.class);
            noticePostService.editNoticePost(postId, request, userDetails, images);
            return ResponseEntity.ok(Map.of("message", "보호소 공지게시판 수정 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 오류", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버오류", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> removeNoticePost(@PathVariable("post_id") Long postId,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        noticePostService.removeNoticePost(postId, userDetails);
        return ResponseEntity.ok(Map.of("message", "보호소 공지게시판 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<List<ShelterNoticePostResponseDTO>> getNoticePostByCreatedAt() {
        List<ShelterNoticePostResponseDTO> res = noticePostService.getAllNoticePost();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<ShelterNoticePostResponseDTO> getDetailNoticePost(@PathVariable("post_id") Long postId) {
        ShelterNoticePostResponseDTO res = noticePostService.getDetailNoticePost(postId);
        return ResponseEntity.ok(res);
    }
}
