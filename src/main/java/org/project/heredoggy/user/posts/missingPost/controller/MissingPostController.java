package org.project.heredoggy.user.posts.missingPost.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.missingPost.dto.MissingPostRequestDTO;
import org.project.heredoggy.user.posts.missingPost.dto.MissingPostResponseDTO;
import org.project.heredoggy.user.posts.missingPost.service.MissingPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/missing-posts")
public class MissingPostController {
    private final MissingPostService missingPostService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createMissingPost(@Valid @RequestBody MissingPostRequestDTO request,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        missingPostService.createMissingPost(request, userDetails);
        return ResponseEntity.ok(Map.of("message", "실종/발견 게시판 생성 완료"));
    }

    @PutMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> editMissingPost(@PathVariable("post_id") Long postId,
                                                            @Valid @RequestBody MissingPostRequestDTO request,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        missingPostService.editMissingPost(postId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "실종/발견 게시판 수정 완료"));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> removeMissingPost(@PathVariable("post_id") Long postId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        missingPostService.removeMissingPost(postId, userDetails);
        return ResponseEntity.ok(Map.of("message", "실종/발견 게시판 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<List<MissingPostResponseDTO>> getMissingPostsByCreatedAt() {
        List<MissingPostResponseDTO> res = missingPostService.getAllMissingPosts();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<MissingPostResponseDTO> getDetailMissingPosts(@PathVariable("post_id") Long postId) {
        MissingPostResponseDTO res = missingPostService.getDetailMissingPosts(postId);
        return ResponseEntity.ok(res);
    }
}
