package org.project.heredoggy.user.report.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.report.member.MemberReport;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.member.dto.MemberReportRequestDTO;
import org.project.heredoggy.user.report.member.service.MemberReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/reports")
public class MemberReportController {
    private final MemberReportService memberReportService;

    @PostMapping
    public ResponseEntity<?> reportMember(@RequestBody MemberReportRequestDTO request,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberReportService.reportMember(userDetails, request);
        return ResponseEntity.ok(Map.of("message", "회원 신고가 접수되었습니다."));
    }
}
