package org.project.heredoggy.domain.postgresql.report.member;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReport;
import org.project.heredoggy.domain.postgresql.report.shelter.ShelterReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberReportRepository extends JpaRepository<MemberReport, Long> {
    boolean existsByReporterAndReported(Member reporter, Member reported);
    List<MemberReport> findAllByReporterOrderByCreatedAtDesc(Member reporter);
    Page<MemberReport> findAllByStatus(ReportStatus status, Pageable pageable);
}
