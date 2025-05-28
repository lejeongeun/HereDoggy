package org.project.heredoggy.domain.postgresql.post.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostTypeAndPostIdOrderByCreatedAt(PostType postType, Long postId);
}
