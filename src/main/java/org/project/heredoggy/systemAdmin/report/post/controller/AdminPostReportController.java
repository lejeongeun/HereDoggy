package org.project.heredoggy.systemAdmin.report.post.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.post.dto.AdminPostReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.post.dto.AdminPostReportResponseDTO;
import org.project.heredoggy.systemAdmin.report.post.dto.ReportActionRequestDTO;
import org.project.heredoggy.systemAdmin.report.post.service.AdminPostReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports/posts")
public class AdminPostReportController {
    private final AdminPostReportService adminPostReportService;

    @GetMapping
    public Page<AdminPostReportResponseDTO> getPostsReports(@RequestParam(required = false) String status,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                            Pageable pageable) {
        return adminPostReportService.getAllPostReports(status, pageable, userDetails);
    }

    @GetMapping("/{report_id}")
    public ResponseEntity<AdminPostReportDetailDTO> getPostDetailReport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable("report_id") Long reportId) {
        AdminPostReportDetailDTO res = adminPostReportService.getPostReportDetail(userDetails, reportId);
        return ResponseEntity.ok(res);
    }

        @PatchMapping("/{report_id}")
        public ResponseEntity<Map<String, String>> handlePostReportAction(@PathVariable("report_id") Long reportId,
                                                                          @RequestBody ReportActionRequestDTO request,
                                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
            adminPostReportService.handlePostReportAction(reportId, request, userDetails);
            return ResponseEntity.ok(Map.of("message", "조치 완료"));
        }
}
