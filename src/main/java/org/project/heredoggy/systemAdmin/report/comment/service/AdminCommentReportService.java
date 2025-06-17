package org.project.heredoggy.systemAdmin.report.comment.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReport;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReportRepository;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReport;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AdminAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.comment.dto.AdminCommentReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.comment.dto.AdminCommentReportResponseDTO;
import org.project.heredoggy.systemAdmin.report.post.dto.ReportActionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCommentReportService {
    private final CommentReportRepository commentReportRepository;
    public Page<AdminCommentReportResponseDTO> getAllCommentReports(String status, Pageable pageable, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        ReportStatus reportStatus = null;

        if (status != null && !status.isBlank()) {
            try {
                reportStatus = ReportStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 신고 상태입니다.");
            }
        }

        Page<CommentReport> reports = (reportStatus == null)
                ? commentReportRepository.findAll(pageable)
                : commentReportRepository.findAllByStatus(reportStatus, pageable);

        return reports.map(report -> AdminCommentReportResponseDTO.builder()
                .id(report.getId())
                .commentId(report.getComment().getId())
                .commentContentSnapshot(report.getCommentContentSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .writerNickname(report.getWriterNicknameSnapshot())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .reportedAt(report.getCreatedAt())
                .build()
        );
    }

    public AdminCommentReportDetailDTO getCommentReportDetail(CustomUserDetails userDetails, Long commentId) {
        AdminAuthUtils.getValidMember(userDetails);

        CommentReport report = commentReportRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));

        return AdminCommentReportDetailDTO.builder()
                .id(report.getId())
                .commentId(report.getComment().getId())
                .commentContentSnapshot(report.getCommentContentSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .writerNickname(report.getWriterNicknameSnapshot())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .adminMemo(report.getAdminMemo())
                .reportedAt(report.getCreatedAt())
                .build();
    }

    public void handleCommentReportAction(Long reportId, ReportActionRequestDTO request, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);

        CommentReport report = commentReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));

        report.setAdminMemo(request.getAdminMemo());
        report.setStatus(ReportStatus.RESOLVED);
    }
}
