package org.project.heredoggy.domain.postgresql.report.shelter;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.project.heredoggy.domain.postgresql.report.comment.CommentReport;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterReportRepository extends JpaRepository<ShelterReport, Long> {
    boolean existsByReporterAndShelter(Member reporter, Shelter shelter);
    List<ShelterReport> findAllByReporterOrderByCreatedAtDesc(Member reporter);
    Page<ShelterReport> findAllByStatus(ReportStatus status, Pageable pageable);
}
