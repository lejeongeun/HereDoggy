package org.project.heredoggy.user.posts.freePost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.freePost.dto.FreePostEditRequestDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostRequestDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostResDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostResponseDTO;
import org.project.heredoggy.user.posts.freePost.service.FreePostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/free-posts")
public class FreePostController {
    private final FreePostService freePostService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createFreePost(
            @Valid @RequestPart("info") String infoJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            FreePostRequestDTO request = objectMapper.readValue(infoJson, FreePostRequestDTO.class);
            freePostService.createFreePost(request, userDetails, images);
            return ResponseEntity.ok(Map.of("message", "자유게시판 생성 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 실패", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버오류", "error", e.getMessage()));
        }
    }

    @PutMapping(value = "/{post_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> editFreePost(
            @PathVariable("post_id") Long postId,
            @RequestPart("info") String infoJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            FreePostEditRequestDTO request = objectMapper.readValue(infoJson, FreePostEditRequestDTO.class);
            freePostService.editFreePost(postId, request, userDetails, images);
            return ResponseEntity.ok(Map.of("message", "자유게시판 수정 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "JSON 파싱 실패", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "서버오류", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> removeFreePost(@PathVariable("post_id") Long postId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        freePostService.removeFreePost(postId, userDetails);
        return ResponseEntity.ok(Map.of("message", "자유게시판 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<List<FreePostResDTO>> getFreePostsByCreatedAt() {
        List<FreePostResDTO> res = freePostService.getAllFreePosts();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<FreePostResponseDTO> getDetailFreePosts(@PathVariable("post_id") Long postId) {
        FreePostResponseDTO res = freePostService.getDetailFreePosts(postId);
        return ResponseEntity.ok(res);
    }
}
