package org.project.heredoggy.dog.dogComment.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dogComment.dto.DogCommentRequestDto;
import org.project.heredoggy.dog.dogComment.dto.DogCommentResponseDto;
import org.project.heredoggy.dog.dogComment.service.DogCommentService;
import org.project.heredoggy.global.util.ApiResponse;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/dogs/{dog_id}/comments")
@RequiredArgsConstructor
public class DogCommentController {
    private final DogCommentService dogCommentService;
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createComment(@PathVariable("dog_id") Long dogId,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestBody DogCommentRequestDto requestDto){
        dogCommentService.createComment(dogId, userDetails.getMember(), requestDto);
        return ResponseEntity.ok(ApiResponse.success("댓글이 생성되었습니다."));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<DogCommentResponseDto>>> getAllComments(@PathVariable("dog_id") Long dogId){
        List<DogCommentResponseDto> commentList = dogCommentService.getAllComments(dogId);
        return ResponseEntity.ok(ApiResponse.success("댓글 조회 성공", commentList));
    }

    @PatchMapping("/{comment_id}")
    public ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable("dog_id") Long dogId,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable("comment_id") Long commentId,
                                                             @RequestBody DogCommentRequestDto requestDto){
        dogCommentService.updateComment(dogId, userDetails.getMember(), commentId, requestDto);
        return ResponseEntity.ok(ApiResponse.success( "댓글이 수정되었습니다."));
    }
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable("dog_id") Long dogId,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable("comment_id") Long commentId){
        dogCommentService.deleteComment(dogId, userDetails.getMember(), commentId);
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다."));
    }
}
