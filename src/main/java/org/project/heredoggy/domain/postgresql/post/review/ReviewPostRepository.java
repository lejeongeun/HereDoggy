package org.project.heredoggy.domain.postgresql.post.review;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.user.posts.reveiwPost.dto.ReviewPostResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {
    @Query("SELECT r FROM ReviewPost r ORDER BY r.createdAt DESC")
    List<ReviewPost> findAllOrderByCreatedAtDesc();

    List<ReviewPost> findByWriterOrderByCreatedAtDesc(Member writer);

    @Query(value = """
    SELECT rp.id AS id,
           rp.title AS title,
           rp.content AS content,
           rp.view_count AS viewCount,
           TO_CHAR(rp.created_at, 'YYYY-MM-DD HH24:MI:SS') AS createdAt,
           m.email AS email,
           m.nickname AS nickname,
           COUNT(DISTINCT c.id) AS commentCount,
           COUNT(DISTINCT l.id) AS likeCount,
           (
                SELECT pi.image_url
                FROM post_image pi
                WHERE pi.review_post_id = rp.id
                AND pi.image_url IS NOT NULL
                ORDER BY pi.id
                LIMIT 1
           ) AS imageUrl
    FROM review_post rp
    JOIN member m ON rp.writer_id = m.id
    LEFT JOIN comment c ON c.post_id = rp.id AND c.post_type = 'REVIEW'
    LEFT JOIN post_like l ON l.review_post_id = rp.id    
    GROUP BY rp.id, m.email, m.nickname, rp.title, rp.content, rp.view_count, rp.created_at
    ORDER BY rp.created_at DESC
""", nativeQuery = true)
    List<ReviewPostResDTO> findAllOptimized();
}
