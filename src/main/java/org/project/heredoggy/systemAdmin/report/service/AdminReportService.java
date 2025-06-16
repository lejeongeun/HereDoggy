package org.project.heredoggy.systemAdmin.report.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReportRepository;
import org.project.heredoggy.domain.postgresql.report.member.MemberReportRepository;
import org.project.heredoggy.domain.postgresql.report.post.PostReportRepository;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReportRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReportService {
    private final PostReportRepository postReportRepository;
    private final CommentReportRepository commentReportRepository;
    private final MemberReportRepository memberReportRepository;
    private final ShelterReportRepository shelterReportRepository;


}
