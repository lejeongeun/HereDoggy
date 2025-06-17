package org.project.heredoggy.user.report.common.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReportRepository;
import org.project.heredoggy.domain.postgresql.report.member.MemberReportRepository;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReportRepository;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.common.dto.MyReportResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportQueryService {
    private final CommentReportRepository commentReportRepository;
    private final ShelterReportRepository shelterReportRepository;
    private final MemberReportRepository memberReportRepository;


    public List<MyReportResponseDTO> getMyCommentReports(CustomUserDetails userDetails) {
        Member reporter = AuthUtils.getValidMember(userDetails);
        return commentReportRepository.findAllByReporterOrderByCreatedAtDesc(reporter).stream()
                .map(MyReportResponseDTO::fromCommentReport)
                .collect(Collectors.toList());
    }

    public List<MyReportResponseDTO> getMyMemberReports(CustomUserDetails userDetails) {
        Member reporter = AuthUtils.getValidMember(userDetails);
        return memberReportRepository.findAllByReporterOrderByCreatedAtDesc(reporter).stream()
                .map(MyReportResponseDTO::fromMemberReport)
                .collect(Collectors.toList());
    }

    public List<MyReportResponseDTO> getMyShelterReports(CustomUserDetails userDetails) {
        Member reporter = AuthUtils.getValidMember(userDetails);
        return shelterReportRepository.findAllByReporterOrderByCreatedAtDesc(reporter).stream()
                .map(MyReportResponseDTO::fromShelterReport)
                .collect(Collectors.toList());
    }
}
