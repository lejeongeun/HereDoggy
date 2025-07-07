package org.project.heredoggy.user.posts.missingPost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.missingPost.dto.*;
import org.project.heredoggy.user.posts.missingPost.service.MissingPostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/missing-posts")
public class MissingPostController {
    private final MissingPostService missingPostService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createMissingPost(
            @RequestPart("info") String infoJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            MissingPostRequestDTO request = objectMapper.readValue(infoJson, MissingPostRequestDTO.class);
            missingPostService.createMissingPost(request, userDetails, images);
            return ResponseEntity.ok(Map.of("message", "실종/발견 게시판 생성 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 실패", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버오류","error", e.getMessage()));
        }

    }

    @PutMapping(value = "/{post_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> editMissingPost(
            @PathVariable("post_id") Long postId,
            @RequestPart("info") String infoJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            MissingPostEditRequestDTO request = objectMapper.readValue(infoJson, MissingPostEditRequestDTO.class);
            missingPostService.editMissingPost(postId, request, userDetails, images);
            return ResponseEntity.ok(Map.of("message", "실종/발견 게시판 수정 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 실패", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버오류", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> removeMissingPost(@PathVariable("post_id") Long postId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        missingPostService.removeMissingPost(postId, userDetails);
        return ResponseEntity.ok(Map.of("message", "실종/발견 게시판 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<List<MissingPostResDTO>> getMissingPostsByCreatedAt() {
        List<MissingPostResDTO> res = missingPostService.getAllMissingPosts();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<MissingPostResponseDTO> getDetailMissingPosts(@PathVariable("post_id") Long postId) {
        MissingPostResponseDTO res = missingPostService.getDetailMissingPosts(postId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/dog-info")
    public ResponseEntity<DogInfoDTO> getDogInfoForPost(@RequestParam Long walkId) {
        DogInfoDTO dogInfo = missingPostService.getDogInfoByWalkId(walkId);
        return ResponseEntity.ok(dogInfo);
    }
}
