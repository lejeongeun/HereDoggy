package org.project.heredoggy.domain.postgresql.walk.walkRecord;

import org.project.heredoggy.user.walk.walkRecord.dto.WalkSimpleStatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {
    @Query("""
        SELECT new org.project.heredoggy.user.walk.walkRecord.dto.WalkSimpleStatisticDTO(
            CAST(COALESCE(SUM(w.actualDistance), 0) AS double ) ,
            CAST(COALESCE(SUM(w.actualDuration), 0) AS integer ) ,
            COUNT(w)
        )
        FROM WalkRecord w
        WHERE w.reservation.member.id = :memberId
          AND w.status = org.project.heredoggy.domain.postgresql.walk.walkRecord.WalkRecordStatus.COMPLETED
    """)
    WalkSimpleStatisticDTO getSimpleStatisticByMemberId(@Param("memberId") Long memberId);

}
