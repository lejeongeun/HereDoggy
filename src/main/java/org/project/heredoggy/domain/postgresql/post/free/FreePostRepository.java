package org.project.heredoggy.domain.postgresql.post.free;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.user.posts.freePost.dto.FreePostResDTO;
import org.project.heredoggy.user.posts.freePost.dto.FreePostResponseDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreePostRepository extends JpaRepository<FreePost, Long> {

    @EntityGraph(attributePaths = {"writer", "postImages"})
    @Query("SELECT f FROM FreePost f ORDER BY f.createdAt DESC")
    List<FreePost> findAllWriterAndImages();
    @Query("SELECT f FROM FreePost f ORDER BY f.createdAt DESC")
    List<FreePost> findAllOrderByCreatedAtDesc();

    @Query(value = """
    SELECT 
        f.id AS id,
        f.title AS title,
        f.content AS content,
        f.view_count AS viewCount,
        TO_CHAR(f.created_at, 'YYYY-MM-DD HH24:MI:SS') AS createdAt,
        m.email AS email,
        m.nickname AS nickname,
        (SELECT COUNT(*) FROM comment c WHERE c.post_type = 'FREE' AND c.post_id = f.id) AS commentCount,
        (SELECT COUNT(*) FROM post_like l WHERE l.free_post_id = f.id) AS likeCount,
        (
                SELECT pi.image_url
                FROM post_image pi
                WHERE pi.free_post_id = f.id
                AND pi.image_url IS NOT NULL
                ORDER BY pi.id
                LIMIT 1
           ) AS imageUrl
    FROM free_post f
    JOIN member m ON f.writer_id = m.id
    LEFT JOIN post_image pi ON pi.free_post_id = f.id
    GROUP BY f.id, f.title, f.content, f.view_count, f.created_at, m.email, m.nickname
    ORDER BY f.created_at DESC
""", nativeQuery = true)
    List<FreePostResDTO> findAllProjected();

    List<FreePost> findByWriterOrderByCreatedAtDesc(Member writer);
}
