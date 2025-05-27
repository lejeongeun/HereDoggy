package org.project.heredoggy.domain.postgresql.post.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {
    @Query("SELECT r FROM ReviewPost r ORDER BY r.createdAt DESC")
    List<ReviewPost> findAllOrderByCreatedAtDesc();
}
