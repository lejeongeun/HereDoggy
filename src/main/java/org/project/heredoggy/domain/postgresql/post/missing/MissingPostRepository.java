package org.project.heredoggy.domain.postgresql.post.missing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissingPostRepository extends JpaRepository<MissingPost, Long> {
    @Query("SELECT m FROM MissingPost m ORDER BY m.createdAt DESC")
    List<MissingPost> findAllOrderByCreatedAtDesc();
}
