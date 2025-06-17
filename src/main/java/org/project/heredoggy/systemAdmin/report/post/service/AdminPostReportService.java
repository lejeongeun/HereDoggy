package org.project.heredoggy.systemAdmin.report.post.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.post.PostReport;
import org.project.heredoggy.domain.postgresql.report.post.PostReportRepository;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AdminAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.post.dto.AdminPostReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.post.dto.AdminPostReportResponseDTO;
import org.project.heredoggy.systemAdmin.report.post.dto.ReportActionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPostReportService {
    private final PostReportRepository postReportRepository;

    public Page<AdminPostReportResponseDTO> getAllPostReports(String status, Pageable pageable, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        ReportStatus reportStatus = null;

        if (status != null && !status.isBlank()) {
            try {
                reportStatus = ReportStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 신고 상태입니다.");
            }
        }

        Page<PostReport> reports = (reportStatus == null)
                ? postReportRepository.findAll(pageable)
                : postReportRepository.findAllByStatus(reportStatus, pageable);

        return reports.map(report -> AdminPostReportResponseDTO.builder()
                .id(report.getId())
                .postTitleSnapshot(report.getPostTitleSnapshot())
                .postContentSnapshot(report.getPostContentSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .writerNickname(report.getWriterNicknameSnapshot())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .type(report.getPostType())
                .postId(report.getPostId())
                .reportedAt(report.getCreatedAt())
                .build()
        );
    }

    public AdminPostReportDetailDTO getPostReportDetail(CustomUserDetails userDetails, Long reportId) {
        AdminAuthUtils.getValidMember(userDetails);

        PostReport report = postReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));

        return AdminPostReportDetailDTO.builder()
                .id(report.getId())
                .postId(report.getPostId())
                .postTitleSnapshot(report.getPostTitleSnapshot())
                .postContentSnapshot(report.getPostContentSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .writerNickname(report.getWriterNicknameSnapshot())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .adminMemo(report.getAdminMemo())
                .reportedAt(report.getCreatedAt())
                .build();
    }

    @Transactional
    public void handlePostReportAction(Long reportId, ReportActionRequestDTO request, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);

        PostReport report = postReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));

        report.setAdminMemo(request.getAdminMemo());
        report.setStatus(ReportStatus.RESOLVED);
    }
}
