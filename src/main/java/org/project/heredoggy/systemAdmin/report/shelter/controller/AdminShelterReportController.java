package org.project.heredoggy.systemAdmin.report.shelter.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.comment.dto.AdminCommentReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.post.dto.ReportActionRequestDTO;
import org.project.heredoggy.systemAdmin.report.shelter.dto.AdminShelterReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.shelter.dto.AdminShelterReportResponseDTO;
import org.project.heredoggy.systemAdmin.report.shelter.service.AdminShelterReportService;
import org.project.heredoggy.user.report.shelter.service.ShelterReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports/shelter")
public class AdminShelterReportController {

    private final AdminShelterReportService adminShelterReportService;

    @GetMapping
    public Page<AdminShelterReportResponseDTO> getSheltersReports(@RequestParam(required = false) String status,
                                                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  Pageable pageable) {
        return adminShelterReportService.getAllShelterReports(status, pageable, userDetails);
    }

    @GetMapping("/{shelter_id}")
    public ResponseEntity<AdminShelterReportDetailDTO> getPostDetailReport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @PathVariable("shelter_id") Long shelterId) {
        AdminShelterReportDetailDTO res = adminShelterReportService.getShelterReportDetail(userDetails, shelterId);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{report_id}")
    public ResponseEntity<Map<String, String>> handleCommentReportAction(@PathVariable("report_id") Long reportId,
                                                                         @RequestBody ReportActionRequestDTO request,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminShelterReportService.handleShelterReportAction(reportId, request, userDetails);
        return ResponseEntity.ok(Map.of("message", "조치 완료"));
    }
}
