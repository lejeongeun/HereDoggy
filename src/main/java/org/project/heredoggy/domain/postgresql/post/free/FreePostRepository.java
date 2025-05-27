package org.project.heredoggy.domain.postgresql.post.free;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreePostRepository extends JpaRepository<FreePost, Long> {
    @Query("SELECT f FROM FreePost f ORDER BY f.createdAt DESC")
    List<FreePost> findAllOrderByCreatedAtDesc();
}
