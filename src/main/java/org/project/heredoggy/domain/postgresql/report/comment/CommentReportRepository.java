package org.project.heredoggy.domain.postgresql.report.comment;

import org.project.heredoggy.domain.postgresql.comment.Comment;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    boolean existsByReporterAndComment(Member reporter, Comment comment);
    List<CommentReport> findAllByReporterOrderByCreatedAtDesc(Member reporter);
}
