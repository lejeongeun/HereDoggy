package org.project.heredoggy.systemAdmin.report.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.comment.dto.AdminCommentReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.member.dto.AdminMemberReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.member.dto.AdminMemberReportResponseDTO;
import org.project.heredoggy.systemAdmin.report.member.service.AdminMemberReportService;
import org.project.heredoggy.systemAdmin.report.post.dto.ReportActionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports/members")
public class AdminMemberReportController {
    private final AdminMemberReportService adminMemberReportService;

    @GetMapping
    public Page<AdminMemberReportResponseDTO> getMembersReports(@RequestParam(required = false) String status,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                Pageable pageable) {
        return adminMemberReportService.getAllMemberReports(status, pageable, userDetails);
    }

    @GetMapping("/{report_id}")
    public ResponseEntity<AdminMemberReportDetailDTO> getCommentDetailReport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                              @PathVariable("report_id") Long reportId) {
        AdminMemberReportDetailDTO res = adminMemberReportService.getMemberReportDetail(userDetails, reportId);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{report_id}")
    public ResponseEntity<Map<String, String>> handleCommentReportAction(@PathVariable("report_id") Long reportId,
                                                                      @RequestBody ReportActionRequestDTO request,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminMemberReportService.handleCommentReportAction(reportId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "조치 완료"));
    }
}
