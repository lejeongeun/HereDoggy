package org.project.heredoggy.domain.postgresql.post.missing;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.user.posts.missingPost.dto.MissingPostResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissingPostRepository extends JpaRepository<MissingPost, Long> {
    @Query("SELECT m FROM MissingPost m ORDER BY m.createdAt DESC")
    List<MissingPost> findAllOrderByCreatedAtDesc();

    List<MissingPost> findByWriterOrderByCreatedAtDesc(Member writer);

    @Query(value = """
    SELECT mp.id AS id,
           mp.title AS title,
           mp.description AS content,
           mp.view_count AS viewCount,
           TO_CHAR(mp.created_at, 'YYYY-MM-DD HH24:MI:SS') AS createdAt,
           m.email AS email,
           m.nickname AS nickname,
           COUNT(DISTINCT c.id) AS commentCount,
           COUNT(DISTINCT l.id) AS likeCount,
           (
               SELECT pi.image_url
               FROM post_image pi
               WHERE pi.missing_post_id = mp.id
                 AND pi.image_url IS NOT NULL
               ORDER BY pi.id
               LIMIT 1
           ) AS imageUrl
    FROM missing_post mp
    JOIN member m ON mp.writer_id = m.id
    LEFT JOIN comment c ON c.post_id = mp.id AND c.post_type = 'MISSING'
    LEFT JOIN post_like l ON l.missing_post_id = mp.id
    GROUP BY mp.id, m.email, m.nickname, mp.title, mp.description, mp.view_count, mp.created_at
    ORDER BY mp.created_at DESC
""", nativeQuery = true)
    List<MissingPostResDTO> findAllOptimized();
}
