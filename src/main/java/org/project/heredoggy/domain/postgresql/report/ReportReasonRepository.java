package org.project.heredoggy.domain.postgresql.report;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportReasonRepository extends JpaRepository<ReportReason, Long> {
    List<ReportReason> findAllByTargetTypeAndIsActiveTrue(ReportTargetType type);
}
