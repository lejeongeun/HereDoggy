package org.project.heredoggy.systemAdmin.report.member.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.notification.ReferenceType;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.member.MemberReport;
import org.project.heredoggy.domain.postgresql.report.member.MemberReportRepository;
import org.project.heredoggy.domain.postgresql.report.post.PostReport;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.NotificationFactory;
import org.project.heredoggy.global.util.AdminAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.member.dto.AdminMemberReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.member.dto.AdminMemberReportResponseDTO;
import org.project.heredoggy.systemAdmin.report.post.dto.ReportActionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminMemberReportService {
    private final MemberReportRepository memberReportRepository;
    private final NotificationFactory notificationFactory;

    public Page<AdminMemberReportResponseDTO> getAllMemberReports(String status, Pageable pageable, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        ReportStatus reportStatus = null;

        if (status != null && !status.isBlank()) {
            try {
                reportStatus = ReportStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 신고 상태입니다.");
            }
        }

        Page<MemberReport> reports = (reportStatus == null)
                ? memberReportRepository.findAll(pageable)
                : memberReportRepository.findAllByStatus(reportStatus, pageable);

        return reports.map(report -> AdminMemberReportResponseDTO.builder()
                .id(report.getId())
                .reportedMemberId(report.getReported().getId())
                .memberNicknameSnapshot(report.getWriterNicknameSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .reportedAt(report.getCreatedAt())
                .build()
        );
    }

    public AdminMemberReportDetailDTO getMemberReportDetail(CustomUserDetails userDetails, Long reportId) {
        AdminAuthUtils.getValidMember(userDetails);

        MemberReport report = memberReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));

        return AdminMemberReportDetailDTO.builder()
                .id(report.getId())
                .reportedMemberId(report.getReported().getId())
                .memberNicknameSnapshot(report.getWriterNicknameSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .adminMemo(report.getAdminMemo())
                .reportedAt(report.getCreatedAt())
                .build();
    }

    public void handleCommentReportAction(Long reportId, ReportActionRequestDTO request, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);

        MemberReport report = memberReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));

        report.setAdminMemo(request.getAdminMemo());
        report.setStatus(ReportStatus.RESOLVED);

        notificationFactory.notifyReportResolved(
                report.getReporter(),
                report.getId(),
                ReferenceType.MEMBER
        );
    }
}
