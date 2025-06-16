package org.project.heredoggy.systemAdmin.report.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReportRepository;
import org.project.heredoggy.domain.postgresql.report.member.MemberReportRepository;
import org.project.heredoggy.domain.postgresql.report.post.PostReport;
import org.project.heredoggy.domain.postgresql.report.post.PostReportRepository;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReportRepository;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.report.dto.AdminPostReportResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReportService {
    private final PostReportRepository postReportRepository;
    private final CommentReportRepository commentReportRepository;
    private final MemberReportRepository memberReportRepository;
    private final ShelterReportRepository shelterReportRepository;


    public Page<AdminPostReportResponseDTO> getAllPostReports(String status, Pageable pageable, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        ReportStatus reportStatus = null;

        if(status != null && !status.isBlank()) {
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
                .postType(report.getPostType())
                .postId(report.getPostId())
                .postTitle(report.getPostTitleSnapshot())
                .reporterNickname(report.getReporter().getNickname())
                .writerNickname(report.getWriterNicknameSnapshot())
                .reason(report.getReason().getContent())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .build()
        );
    }

    public Page<AdminPostReportResponseDTO> getAllCommentReports(String status, Pageable pageable, CustomUserDetails userDetails) {

    }

    public Page<AdminPostReportResponseDTO> getAllMemberReports(String status, Pageable pageable, CustomUserDetails userDetails) {

    }

    public Page<AdminPostReportResponseDTO> getAllShelterReports(String status, Pageable pageable, CustomUserDetails userDetails) {

    }
}
