package org.project.heredoggy.user.report.comment.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.comment.dto.CommentReportRequestDTO;
import org.project.heredoggy.user.report.comment.service.CommentReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/comments")
public class CommentReportController {
    private final CommentReportService commentReportService;

    @PostMapping
    public ResponseEntity<?> reportComment(@RequestBody CommentReportRequestDTO request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentReportService.reportComment(userDetails, request);
        return ResponseEntity.ok(Map.of("message", "댓글 신고가 접수되었습니다."));
    }
}
