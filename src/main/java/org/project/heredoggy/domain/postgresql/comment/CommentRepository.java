package org.project.heredoggy.domain.postgresql.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostTypeAndPostIdOrderByCreatedAt(PostType postType, Long postId);

    Long countByPostTypeAndPostId(PostType postType, Long postId);

    @Query("SELECT c.postId AS postId, COUNT(c) AS cnt " +
            "FROM Comment c WHERE c.postType = :postType AND c.postId IN :postIds GROUP BY c.postId")
    List<CommentCountProjection> countCommentsByPostIds(@Param("postIds") List<Long> postIds,
                                                        @Param("postType") PostType postType);
}

