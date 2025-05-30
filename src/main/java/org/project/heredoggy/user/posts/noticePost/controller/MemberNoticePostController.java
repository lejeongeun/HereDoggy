package org.project.heredoggy.user.posts.noticePost.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostResponseDTO;
import org.project.heredoggy.shelter.noticePost.service.ShelterNoticePostService;
import org.project.heredoggy.user.posts.noticePost.service.MemberNoticePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/notice-posts")
public class MemberNoticePostController {
    private final MemberNoticePostService noticePostService;

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
