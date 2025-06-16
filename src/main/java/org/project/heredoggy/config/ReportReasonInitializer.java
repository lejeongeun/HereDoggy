package org.project.heredoggy.config;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.report.ReportReason;
import org.project.heredoggy.domain.postgresql.report.ReportReasonRepository;
import org.project.heredoggy.domain.postgresql.report.ReportTargetType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportReasonInitializer implements CommandLineRunner {

    private final ReportReasonRepository reportReasonRepository;

    @Override
    public void run(String... args) {
        if (reportReasonRepository.count() == 0) {
            List<ReportReason> reasons = List.of(
                    // 게시글 신고 사유 (POST)
                    new ReportReason("성적인 내용이 포함되어 있습니다", ReportTargetType.POST),
                    new ReportReason("폭력적인 표현이 포함되어 있습니다", ReportTargetType.POST),
                    new ReportReason("혐오/차별적 표현이 포함되어 있습니다", ReportTargetType.POST),
                    new ReportReason("광고 또는 홍보 목적의 게시물입니다", ReportTargetType.POST),
                    new ReportReason("잘못된 정보 또는 거짓 정보입니다", ReportTargetType.POST),
                    new ReportReason("도배/중복 게시물입니다", ReportTargetType.POST),
                    new ReportReason("게시글 주제와 무관한 내용입니다", ReportTargetType.POST),

                    // 댓글 신고 사유 (COMMENT)
                    new ReportReason("욕설이 포함되어 있습니다", ReportTargetType.COMMENT),
                    new ReportReason("비방/비하하는 표현이 포함되어 있습니다", ReportTargetType.COMMENT),
                    new ReportReason("음란한 표현이 포함되어 있습니다", ReportTargetType.COMMENT),
                    new ReportReason("도배/중복 댓글입니다", ReportTargetType.COMMENT),
                    new ReportReason("내용이 부적절하거나 불쾌합니다", ReportTargetType.COMMENT),
                    new ReportReason("광고 또는 스팸 댓글입니다", ReportTargetType.COMMENT),

                    // 유저 신고 사유 (MEMBER)
                    new ReportReason("부적절한 닉네임을 사용하고 있습니다", ReportTargetType.MEMBER),
                    new ReportReason("악의적으로 다른 유저를 괴롭히고 있습니다", ReportTargetType.MEMBER),
                    new ReportReason("부적절한 게시글/댓글을 반복적으로 작성하고 있습니다", ReportTargetType.MEMBER),
                    new ReportReason("타인의 정보를 도용한 계정입니다", ReportTargetType.MEMBER),
                    new ReportReason("기타 계정 관련 부정 행위가 있습니다", ReportTargetType.MEMBER),

                    // 보호소 신고 사유 (SHELTER)
                    new ReportReason("등록된 정보가 실제와 다릅니다", ReportTargetType.SHELTER),
                    new ReportReason("보호소 운영이 비정상적으로 보입니다", ReportTargetType.SHELTER),
                    new ReportReason("보호 동물에 대한 학대 정황이 의심됩니다", ReportTargetType.SHELTER),
                    new ReportReason("입양/산책 관련 부정확한 정보가 있습니다", ReportTargetType.SHELTER),
                    new ReportReason("연락이 되지 않거나 응답이 없습니다", ReportTargetType.SHELTER)
            );

            reportReasonRepository.saveAll(reasons);
        }
    }
}
