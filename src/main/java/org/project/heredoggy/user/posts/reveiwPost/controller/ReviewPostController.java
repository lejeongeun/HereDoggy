package org.project.heredoggy.user.posts.reveiwPost.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.reveiwPost.dto.ReviewPostRequestDTO;
import org.project.heredoggy.user.posts.reveiwPost.dto.ReviewPostResponseDTO;
import org.project.heredoggy.user.posts.reveiwPost.service.ReviewPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/review-posts")
public class ReviewPostController {
    private final ReviewPostService reviewPostService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createReviewPost(@Valid @RequestBody ReviewPostRequestDTO request,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewPostService.createReviewPost(request, userDetails);
        return ResponseEntity.ok(Map.of("message", "입양/산책 리뷰 게시판 생성 완료"));
    }

    @PutMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> editReviewPost(@PathVariable("post_id") Long postId,
                                                            @Valid @RequestBody ReviewPostRequestDTO request,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewPostService.editReviewPost(postId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "입양/산책 리뷰 수정 완료"));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> removeReviewPost(@PathVariable("post_id") Long postId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewPostService.removeReviewPost(postId, userDetails);
        return ResponseEntity.ok(Map.of("message", "입양/산책 리뷰 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<List<ReviewPostResponseDTO>> getReviewPostsByCreatedAt(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ReviewPostResponseDTO> res = reviewPostService.getAllReviewPosts(userDetails);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<ReviewPostResponseDTO> getDetailReviewPosts(@PathVariable("post_id") Long postId,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        ReviewPostResponseDTO res = reviewPostService.getDetailReviewPosts(postId, userDetails);
        return ResponseEntity.ok(res);
    }
}
