package org.project.heredoggy.systemAdmin.report.comment.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.comment.dto.AdminCommentReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.comment.dto.AdminCommentReportResponseDTO;
import org.project.heredoggy.systemAdmin.report.comment.service.AdminCommentReportService;
import org.project.heredoggy.systemAdmin.report.post.dto.ReportActionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports/comments")
public class AdminCommentReportController {
    private final AdminCommentReportService adminCommentReportService;

    @GetMapping
    public ResponseEntity<Page<AdminCommentReportResponseDTO>> getCommentsReports(@RequestParam(required = false) String status,
                                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  Pageable pageable) {
        Page<AdminCommentReportResponseDTO> res = adminCommentReportService.getAllCommentReports(status, pageable, userDetails);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{comment_id}")
    public ResponseEntity<AdminCommentReportDetailDTO> getCommentDetailReport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @PathVariable("comment_id") Long commentId) {
        AdminCommentReportDetailDTO res = adminCommentReportService.getCommentReportDetail(userDetails, commentId);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{report_id}")
    public ResponseEntity<Map<String, String>> handleCommentReportAction(@PathVariable("report_id") Long reportId,
                                                                         @RequestBody ReportActionRequestDTO request,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminCommentReportService.handleCommentReportAction(reportId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "조치 완료"));
    }
}
