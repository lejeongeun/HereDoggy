package org.project.heredoggy.user.posts.reveiwPost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.reveiwPost.dto.*;
import org.project.heredoggy.user.posts.reveiwPost.service.ReviewPostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/review-posts")
public class ReviewPostController {
    private final ReviewPostService reviewPostService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createReviewPost(
            @RequestPart("info") String infoJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            ReviewPostRequestDTO request = objectMapper.readValue(infoJson, ReviewPostRequestDTO.class);
            reviewPostService.createReviewPost(request, userDetails, images);
            return ResponseEntity.ok(Map.of("message", "입양/산책 리뷰 게시판 생성 완료"));
        } catch(JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 실패", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버오류", "error", e.getMessage()));
        }
    }

    @PutMapping(value = "/{post_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> editReviewPost(
            @PathVariable("post_id") Long postId,
            @RequestPart("info") String infoJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            ReviewPostEditRequestDTO request = objectMapper.readValue(infoJson, ReviewPostEditRequestDTO.class);
            reviewPostService.editReviewPost(postId, request, userDetails, images);
            return ResponseEntity.ok(Map.of("message", "입양/산책 리뷰 수정 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 실패", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버 오류", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> removeReviewPost(@PathVariable("post_id") Long postId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewPostService.removeReviewPost(postId, userDetails);
        return ResponseEntity.ok(Map.of("message", "입양/산책 리뷰 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<List<ReviewPostResDTO>> getReviewPostsByCreatedAt() {
        List<ReviewPostResDTO> res = reviewPostService.getAllReviewPosts();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<ReviewPostResponseDTO> getDetailReviewPosts(@PathVariable("post_id") Long postId) {
        ReviewPostResponseDTO res = reviewPostService.getDetailReviewPosts(postId);
        return ResponseEntity.ok(res);
    }

    //산책 기준으로 강아지 정보 가져오기
    @GetMapping("/prepare-review/{reservationId}")
    public ResponseEntity<ReviewDogInfoDTO> prepareReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long reservationId) {
        ReviewDogInfoDTO dogInfo = reviewPostService.getDogInfoFromReservation(userDetails, reservationId);
        return ResponseEntity.ok(dogInfo);
    }

    //사용자가 완료한 산책 리스트를 기준으로 강아지 목록 조회
    @GetMapping("/review/dogs")
    public ResponseEntity<List<ReviewDogInfoDTO>> getMyReviewableDogs(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ReviewDogInfoDTO> result = reviewPostService.getMyCompletedDogs(userDetails);
        return ResponseEntity.ok(result);
    }
}
