package org.project.heredoggy.user.posts.noticePost.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.shelter.noticePost.dto.NoticePostResDTO;
import org.project.heredoggy.shelter.noticePost.dto.ShelterNoticePostResponseDTO;
import org.project.heredoggy.user.posts.noticePost.service.MemberNoticePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/notice-posts")
public class MemberNoticePostController {
    private final MemberNoticePostService noticePostService;

    @GetMapping
    public ResponseEntity<List<NoticePostResDTO>> getNoticePostByShelter(
            @RequestParam("shelterId") Long shelterId) {

        List<NoticePostResDTO> res = noticePostService.getNoticePostsByShelter(shelterId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<ShelterNoticePostResponseDTO> getDetailNoticePost(@PathVariable("post_id") Long postId) {
        ShelterNoticePostResponseDTO res = noticePostService.getDetailNoticePost(postId);
        return ResponseEntity.ok(res);
    }
}
