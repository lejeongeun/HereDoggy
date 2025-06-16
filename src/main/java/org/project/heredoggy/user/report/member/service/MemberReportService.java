package org.project.heredoggy.user.report.member.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.domain.postgresql.report.ReportReason;
import org.project.heredoggy.domain.postgresql.report.ReportReasonRepository;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.member.MemberReport;
import org.project.heredoggy.domain.postgresql.report.member.MemberReportRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.AdminSseNotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.member.dto.MemberReportRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberReportService {
    private final MemberReportRepository memberReportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final MemberRepository memberRepository;
    private final AdminSseNotificationFactory sseNotificationFactory;

    public void reportMember(CustomUserDetails userDetails, MemberReportRequestDTO request) {
        Member reporter = AuthUtils.getValidMember(userDetails);

        Member reported = memberRepository.findById(request.getReportedMemberId())
                .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다."));

        if(reporter.getId().equals(reported.getId())) {
            throw new ConflictException("자기 자신은 신고할 수 없습니다.");
        }

        ReportReason reason = reportReasonRepository.findById(request.getReasonId())
                .orElseThrow(() -> new NotFoundException("신고 사유가 존재 하지 않습니다."));

        if(memberReportRepository.existsByReporterAndReported(reporter, reported)) {
            throw new ConflictException("이미 해당 유저를 신고하셨습니다.");
        }

        MemberReport report = MemberReport.builder()
                .reporter(reporter)
                .reported(reported)
                .reason(reason)
                .status(ReportStatus.UNRESOLVED)
                .build();

        memberReportRepository.save(report);

        List<Member> admins = memberRepository.findAllByRole(RoleType.SYSTEM_ADMIN);
        for (Member admin : admins) {
            sseNotificationFactory.notifyMemberReported(
                    admin,
                    reporter.getNickname(),
                    reported.getNickname(),
                    reason.getContent(),
                    reported.getId()
            );
        }
    }
}
