package org.project.heredoggy.shelter.noticePost.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostRequestDTO;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostResponseDTO;
import org.project.heredoggy.shelter.noticePost.service.ShelterNoticePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shelters/notice-posts")
public class ShelterNoticePostController {
    private final ShelterNoticePostService noticePostService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createNoticePost(@Valid @RequestBody ShelterNoticePostRequestDTO request,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        noticePostService.createNoticePost(request, userDetails);
        return ResponseEntity.ok(Map.of("message", "보호소 공지게시판 생성 완료"));
    }

    @PutMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> editNoticePost(@PathVariable("post_id") Long postId,
                                                            @Valid @RequestBody ShelterNoticePostRequestDTO request,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        noticePostService.editNoticePost(postId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "보호소 공지게시판 수정 완료"));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> removeNoticePost(@PathVariable("post_id") Long postId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        noticePostService.removeNoticePost(postId, userDetails);
        return ResponseEntity.ok(Map.of("message", "보호소 공지게시판 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<List<ShelterNoticePostResponseDTO>> getNoticePostByCreatedAt(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ShelterNoticePostResponseDTO> res = noticePostService.getAllNoticePost(userDetails);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<ShelterNoticePostResponseDTO> getDetailNoticePost(@PathVariable("post_id") Long postId,
                                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ShelterNoticePostResponseDTO res = noticePostService.getDetailNoticePost(postId, userDetails);
        return ResponseEntity.ok(res);
    }
}
