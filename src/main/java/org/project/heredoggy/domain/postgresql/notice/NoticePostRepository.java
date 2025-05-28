package org.project.heredoggy.domain.postgresql.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticePostRepository extends JpaRepository<NoticePost, Long> {
    @Query("SELECT n FROM NoticePost n ORDER BY n.createdAt DESC")
    List<NoticePost> findAllOrderByCreatedAtDesc();
}
