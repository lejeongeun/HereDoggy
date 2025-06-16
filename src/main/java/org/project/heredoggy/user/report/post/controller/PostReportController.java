package org.project.heredoggy.user.report.post.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.common.dto.PostReportResponseDTO;
import org.project.heredoggy.user.report.post.dto.PostReportRequestDTO;
import org.project.heredoggy.user.report.post.service.PostReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/posts")
public class PostReportController {
    private final PostReportService postReportService;

    @PostMapping
    public ResponseEntity<Map<String, String>> reportPost(@RequestBody PostReportRequestDTO request,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        postReportService.reportPost(userDetails, request);
        return ResponseEntity.ok(Map.of("message", "신고가 정상적으로 접수 되었습니다."));
    }

    @GetMapping("/me")
    public ResponseEntity<List<PostReportResponseDTO>> getMyPostReports(CustomUserDetails userDetails) {
        List<PostReportResponseDTO> reports = postReportService.getMyPostReports(userDetails);
        return ResponseEntity.ok(reports);
    }
}
