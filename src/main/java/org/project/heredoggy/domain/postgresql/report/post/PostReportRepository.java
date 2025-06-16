package org.project.heredoggy.domain.postgresql.report.post;

import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    boolean existsByReporterAndPostIdAndPostType(Member reporter, Long postId, PostType postType);
    List<PostReport> findAllByReporterOrderByCreatedAtDesc(Member reporter);
}