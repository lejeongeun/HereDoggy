package org.project.heredoggy.user.report.comment.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.comment.Comment;
import org.project.heredoggy.domain.postgresql.comment.CommentRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.domain.postgresql.report.ReportReason;
import org.project.heredoggy.domain.postgresql.report.ReportReasonRepository;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReport;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReportRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.AdminSseNotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.comment.dto.CommentReportRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentReportService {
    private final ReportReasonRepository reportReasonRepository;
    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    private final AdminSseNotificationFactory sseNotificationFactory;
    private final MemberRepository memberRepository;

    @Transactional
    public void reportComment(CustomUserDetails userDetails, CommentReportRequestDTO request) {
        Member reporter = AuthUtils.getValidMember(userDetails);

        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new NotFoundException("댓글이 존재하지 않습니다."));

        ReportReason reason = reportReasonRepository.findById(request.getReasonId())
                .orElseThrow(() -> new NotFoundException("신고 사유가 존재하지 않습니다."));

        if(commentReportRepository.existsByReporterAndComment(reporter, comment)){
            throw new ConflictException("이미 신고한 댓글입니다.");
        }

        CommentReport report = CommentReport.builder()
                .reporter(reporter)
                .comment(comment)
                .reason(reason)
                .status(ReportStatus.UNRESOLVED)
                .build();

        commentReportRepository.save(report);

        List<Member> admins = memberRepository.findAllByRole(RoleType.SYSTEM_ADMIN);
        for (Member admin : admins) {
            sseNotificationFactory.notifyCommentReported(
                    admin,
                    reporter.getNickname(),
                    reason.getContent(),
                    comment.getId()
            );
        }
    }
}
