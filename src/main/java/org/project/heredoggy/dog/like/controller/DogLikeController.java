package org.project.heredoggy.dog.like.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.like.service.DogLikeService;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dogs")
@RequiredArgsConstructor
public class DogLikeController {
    private final DogLikeService dogLikeService;

    @PostMapping("/{dogs_id}")
    public ResponseEntity<?> toggleDogLike(@PathVariable("dogs_id") Long dogId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails){
        boolean liked = dogLikeService.toggleDogLike(dogId, userDetails);
        long likeCount = dogLikeService.getLikeCount(dogId);
        return ResponseEntity.ok(Map.of("message", liked ? "좋아요 등록 " : "좋아요 취소",
                "likedCount", likeCount));
    }
}
