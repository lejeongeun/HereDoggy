package org.project.heredoggy.user.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.comment.dto.CommentRequestDTO;
import org.project.heredoggy.user.comment.dto.CommentResponseDTO;
import org.project.heredoggy.user.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postType}-posts/{post_id}/comments")
    public ResponseEntity<Map<String, String>> createComment(@PathVariable("postType") String postTypeStr,
                                                                @PathVariable("post_id") Long postId,
                                                                @Valid @RequestBody CommentRequestDTO request,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostType postType = PostType.valueOf(postTypeStr.toUpperCase());
        Member writer = AuthUtils.getValidMember(userDetails);
        commentService.createComment(postType, postId, request.getContent(), writer);
        return ResponseEntity.ok(Map.of("message", "댓글 생성 성공"));
    }

    @GetMapping("/{postType}-posts/{post_id}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable("postType") String postTypeStr,
                                                                @PathVariable("post_id") Long postId) {
        PostType postType = PostType.valueOf(postTypeStr.toUpperCase());
        List<CommentResponseDTO> list = commentService.getComments(postType, postId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/comments/{comment_id}")
    public ResponseEntity<?> editComment(@PathVariable("comment_id") Long commentId,
                                         @Valid @RequestBody CommentRequestDTO requestDTO,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member writer = AuthUtils.getValidMember(userDetails);
        commentService.editComments(requestDTO.getContent(), commentId, writer);
        return ResponseEntity.ok(Map.of("message", "댓글 수정 성공"));
    }

    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<?> deleteComments(@PathVariable("comment_id") Long commentId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member writer = AuthUtils.getValidMember(userDetails);
        commentService.deleteComments(commentId,writer);
        return ResponseEntity.ok(Map.of("message", "댓글 삭제 성공"));
    }


}
