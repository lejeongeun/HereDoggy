package org.project.heredoggy.systemAdmin.report.shelter.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.member.MemberReport;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReport;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReportRepository;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AdminAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.post.dto.ReportActionRequestDTO;
import org.project.heredoggy.systemAdmin.report.shelter.dto.AdminShelterReportDetailDTO;
import org.project.heredoggy.systemAdmin.report.shelter.dto.AdminShelterReportResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminShelterReportService {
    private final ShelterReportRepository shelterReportRepository;
    public Page<AdminShelterReportResponseDTO> getAllShelterReports(String status, Pageable pageable, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);
        ReportStatus reportStatus = null;

        if (status != null && !status.isBlank()) {
            try {
                reportStatus = ReportStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 신고 상태입니다.");
            }
        }

        Page<ShelterReport> reports = (reportStatus == null)
                ? shelterReportRepository.findAll(pageable)
                : shelterReportRepository.findAllByStatus(reportStatus, pageable);

        return reports.map(report -> AdminShelterReportResponseDTO.builder()
                .id(report.getId())
                .shelterId(report.getShelter().getId())
                .shelterNameSnapshot(report.getShelterNameSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .reportedAt(report.getCreatedAt())
                .build()
        );
    }

    public AdminShelterReportDetailDTO getShelterReportDetail(CustomUserDetails userDetails, Long shelterId) {
        AdminAuthUtils.getValidMember(userDetails);

        ShelterReport report = shelterReportRepository.findById(shelterId)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));

        return AdminShelterReportDetailDTO.builder()
                .id(report.getId())
                .shelterId(report.getShelter().getId())
                .shelterNameSnapshot(report.getShelterNameSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .adminMemo(report.getAdminMemo())
                .reportedAt(report.getCreatedAt())
                .build();
    }

    public void handleShelterReportAction(Long reportId, ReportActionRequestDTO request, CustomUserDetails userDetails) {
        AdminAuthUtils.getValidMember(userDetails);

        ShelterReport report = shelterReportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("해당 신고 내역이 존재하지 않습니다."));

        report.setAdminMemo(request.getAdminMemo());
        report.setStatus(ReportStatus.RESOLVED);
    }
}
