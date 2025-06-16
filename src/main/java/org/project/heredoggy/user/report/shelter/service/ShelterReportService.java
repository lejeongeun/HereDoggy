package org.project.heredoggy.user.report.shelter.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.domain.postgresql.report.ReportReason;
import org.project.heredoggy.domain.postgresql.report.ReportReasonRepository;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReport;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReportRepository;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.AdminSseNotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.shelter.dto.ShelterReportRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterReportService {
    private final ShelterReportRepository shelterReportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final AdminSseNotificationFactory sseNotificationFactory;
    private final MemberRepository memberRepository;

    @Transactional
    public void reportShelter(CustomUserDetails userDetails, ShelterReportRequestDTO request) {
        Member reporter = AuthUtils.getValidMember(userDetails);

        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, request.getShelterId());

        ReportReason reason = reportReasonRepository.findById(request.getReasonId())
                .orElseThrow(() -> new NotFoundException("신고 사유가 존재하지 않습니다."));

        if(shelterReportRepository.existsByReporterAndShelter(reporter, shelter)) {
            throw new ConflictException("이미 해당 보호소를 신고하였습니다.");
        }
        ShelterReport report = ShelterReport.builder()
                .reporter(reporter)
                .reason(reason)
                .shelter(shelter)
                .status(ReportStatus.UNRESOLVED)
                .build();

        shelterReportRepository.save(report);

        List<Member> admins = memberRepository.findAllByRole(RoleType.SYSTEM_ADMIN);
        for (Member admin : admins) {
            sseNotificationFactory.notifyShelterReported(
                    admin,
                    reporter.getNickname(),
                    reason.getContent(),
                    shelter.getId()
            );
        }
    }
}
