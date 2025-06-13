package org.project.heredoggy.domain.postgresql.post.free;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreePostRepository extends JpaRepository<FreePost, Long> {
    @EntityGraph(attributePaths = {"writer", "postImages"})
    Slice<FreePost> findAllBy(Pageable pageable);

    List<FreePost> findByWriterOrderByCreatedAtDesc(Member writer);
}
