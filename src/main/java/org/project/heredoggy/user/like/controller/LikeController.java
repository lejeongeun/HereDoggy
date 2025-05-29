package org.project.heredoggy.user.like.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.like.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{postType}-posts/{post_id}/likes")
public class LikeController {

    private final LikeService likeService;

    //좋아요 클릭 하면 좋아요 등록됨, 다시 클릭하면 취소됨
    @PostMapping
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable("postType") String postTypeStr,
                                                          @PathVariable("post_id") Long postId,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostType postType = PostType.valueOf(postTypeStr.toUpperCase());
        boolean liked = likeService.toggleLike(postType, postId, userDetails);
        return ResponseEntity.ok(Map.of("message", liked ? "좋아요 등록됨" : "좋아요 취소됨",
                "liked", liked));
    }

    //좋아요 갯수 조회
    @GetMapping("/count")
    public ResponseEntity<Long> toggleLikeCount(@PathVariable("postType") String postTypeStr,
                                                @PathVariable("post_id") Long postId) {
        PostType postType = PostType.valueOf(postTypeStr.toUpperCase());
        Long likeCount = likeService.getLikeCount(postType, postId);
        return ResponseEntity.ok(likeCount);
    }
}
