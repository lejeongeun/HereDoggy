package org.project.heredoggy.systemAdmin.report.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.dto.AdminPostReportResponseDTO;
import org.project.heredoggy.systemAdmin.report.service.AdminReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports")
public class AdminReportController {
    private final AdminReportService adminReportService;

    @GetMapping("/posts")
    public Page<AdminPostReportResponseDTO> getPostsReports(@RequestParam(required = false) String status,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                            Pageable pageable) {
        return adminReportService.getAllPostReports(status, pageable, userDetails);
    }

    @GetMapping("/comments")
    public Page<AdminPostReportResponseDTO> getCommentsReports(@RequestParam(required = false) String status,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails,
                                                               Pageable pageable) {
        return adminReportService.getAllCommentReports(status, pageable, userDetails);
    }

    @GetMapping("/members")
    public Page<AdminPostReportResponseDTO> getMembersReports(@RequestParam(required = false) String status,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails,
                                                              Pageable pageable) {
        return adminReportService.getAllMemberReports(status, pageable, userDetails);
    }

    @GetMapping("/shelters")
    public Page<AdminPostReportResponseDTO> getSheltersReports(@RequestParam(required = false) String status,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                                            Pageable pageable) {
        return adminReportService.getAllShelterReports(status, pageable, userDetails);
    }
}
