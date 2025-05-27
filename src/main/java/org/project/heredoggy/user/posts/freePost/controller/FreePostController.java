package org.project.heredoggy.user.posts.freePost.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.posts.freePost.dto.FreePostRequestDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostResponseDTO;
import org.project.heredoggy.user.posts.freePost.service.FreePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/freePosts")
public class FreePostController {
    private final FreePostService freePostService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createFreePost(@Valid @RequestBody FreePostRequestDTO request,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        freePostService.createFreePost(request, userDetails);
        return ResponseEntity.ok(Map.of("message", "자유게시판 생성 완료"));
    }

    @PutMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> editFreePost(@PathVariable("post_id") Long postId,
                                                            @Valid @RequestBody FreePostRequestDTO request,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        freePostService.editFreePost(postId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "자유게시판 수정 완료"));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Map<String, String>> removeFreePost(@PathVariable("post_id") Long postId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        freePostService.removeFreePost(postId, userDetails);
        return ResponseEntity.ok(Map.of("message", "자유게시판 삭제 완료"));
    }

    @GetMapping
    public ResponseEntity<List<FreePostResponseDTO>> getFreePostsByCreatedAt(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<FreePostResponseDTO> res = freePostService.getAllFreePosts(userDetails);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<FreePostResponseDTO> getDetailFreePosts(@PathVariable("post_id") Long postId,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FreePostResponseDTO res = freePostService.getDetailFreePosts(postId, userDetails);
        return ResponseEntity.ok(res);
    }
}
