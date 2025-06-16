package org.project.heredoggy.user.report.post.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.domain.postgresql.notice.NoticePostRepository;
import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPostRepository;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostRepository;
import org.project.heredoggy.domain.postgresql.report.ReportReason;
import org.project.heredoggy.domain.postgresql.report.ReportReasonRepository;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.post.PostReport;
import org.project.heredoggy.domain.postgresql.report.post.PostReportRepository;
import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.AdminSseNotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.report.common.dto.PostReportResponseDTO;
import org.project.heredoggy.user.report.post.dto.PostReportRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostReportService {
    private final ReportReasonRepository reportReasonRepository;
    private final PostReportRepository postReportRepository;
    private final FreePostRepository freePostRepository;
    private final MissingPostRepository missingPostRepository;
    private final ReviewPostRepository reviewPostRepository;
    private final NoticePostRepository noticePostRepository;
    private final AdminSseNotificationFactory sseNotificationFactory;
    private final MemberRepository memberRepository;

    @Transactional
    public void reportPost(CustomUserDetails userDetails, PostReportRequestDTO request) {
        Member reporter = AuthUtils.getValidMember(userDetails);

        AtomicReference<String> titleSnapshot = new AtomicReference<>("[삭제됨]");
        AtomicReference<String> contentSnapshot = new AtomicReference<>("[삭제됨]");
        AtomicReference<String> nicknameSnapshot = new AtomicReference<>("[알 수 없음]");

        boolean exists = false;

        switch (request.getPostType()) {
            case FREE -> {
                exists = freePostRepository.findById(request.getPostId()).map(post -> {
                    titleSnapshot.set(post.getTitle());
                    contentSnapshot.set(post.getContent());
                    nicknameSnapshot.set(post.getWriter().getNickname());
                    return true;
                }).orElse(false);
            }
            case MISSING -> {
                exists = missingPostRepository.findById(request.getPostId()).map(post -> {
                    titleSnapshot.set(post.getTitle());
                    contentSnapshot.set(post.getDescription());
                    nicknameSnapshot.set(post.getWriter().getNickname());
                    return true;
                }).orElse(false);
            }
            case REVIEW -> {
                exists = reviewPostRepository.findById(request.getPostId()).map(post -> {
                    titleSnapshot.set(post.getTitle());
                    contentSnapshot.set(post.getContent());
                    nicknameSnapshot.set(post.getWriter().getNickname());
                    return true;
                }).orElse(false);
            }
            case NOTICE -> {
                exists = noticePostRepository.findById(request.getPostId()).map(post -> {
                    titleSnapshot.set(post.getTitle());
                    contentSnapshot.set(post.getContent());
                    nicknameSnapshot.set(post.getWriter().getNickname());
                    return true;
                }).orElse(false);
            }
        }

        if (!exists) {
            throw new NotFoundException("해당 게시글이 존재하지 않습니다.");
        }

        ReportReason reason = reportReasonRepository.findById(request.getReasonId())
                .orElseThrow(() -> new NotFoundException("신고 사유가 존재하지 않습니다."));

        if(postReportRepository.existsByReporterAndPostIdAndPostType(reporter, request.getPostId(), request.getPostType())) {
            throw new ConflictException("이미 신고한 게시글입니다.");
        }

        PostReport report = PostReport.builder()
                .reporter(reporter)
                .postId(request.getPostId())
                .postType(request.getPostType())
                .reason(reason)
                .postTitleSnapshot(titleSnapshot.get())
                .postContentSnapshot(contentSnapshot.get())
                .writerNicknameSnapshot(nicknameSnapshot.get())
                .status(ReportStatus.UNRESOLVED)
                .build();

        postReportRepository.save(report);

        List<Member> admins = memberRepository.findAllByRole(RoleType.SYSTEM_ADMIN);

        for (Member admin : admins) {
            sseNotificationFactory.notifyPostReported(
                    admin,
                    reporter.getNickname(),
                    reason.getContent(),
                    request.getPostType(),
                    request.getPostId()
            );
        }
    }

    public List<PostReportResponseDTO> getMyPostReports(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        return postReportRepository.findAllByReporterOrderByCreatedAtDesc(member).stream()
                .map(PostReportResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
