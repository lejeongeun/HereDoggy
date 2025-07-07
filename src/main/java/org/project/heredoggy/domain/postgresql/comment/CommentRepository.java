package org.project.heredoggy.domain.postgresql.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostTypeAndPostIdOrderByCreatedAt(PostType postType, Long postId);
    Long countByPostIdAndPostType(Long postId, PostType postType);

    @Query("SELECT c.postId AS postId, COUNT(c) AS count " + "FROM Comment c WHERE c.postType = :postType GROUP BY c.postId")
    List<CommentCountProjection> countCommentsByPostTypeGroupByPostId(@Param("postType") PostType postType);

    public interface CommentCountProjection {
        Long getPostId();
        Long getCount();
    }
}
