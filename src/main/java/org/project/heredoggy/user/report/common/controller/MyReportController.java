package org.project.heredoggy.user.report.common.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.common.dto.MyReportResponseDTO;
import org.project.heredoggy.user.report.common.service.ReportQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/reports")
public class MyReportController {
    private final ReportQueryService reportQueryService;

    @GetMapping("/comments")
    public ResponseEntity<List<MyReportResponseDTO>> getMyCommentReports(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reportQueryService.getMyCommentReports(userDetails));
    }
    @GetMapping("/shelters")
    public ResponseEntity<List<MyReportResponseDTO>> getMyMemberReports(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reportQueryService.getMyMemberReports(userDetails));
    }
    @GetMapping("/members")
    public ResponseEntity<List<MyReportResponseDTO>> getMyShelterReports(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(reportQueryService.getMyShelterReports(userDetails));
    }
}
