package org.project.heredoggy.domain.postgresql.notice;

import org.project.heredoggy.user.posts.freePost.dto.FreePostResDTO;
import org.project.heredoggy.user.posts.noticePost.dto.MemberNoticePostResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticePostRepository extends JpaRepository<NoticePost, Long> {
    @Query("SELECT n FROM NoticePost n ORDER BY n.createdAt DESC")
    List<NoticePost> findAllOrderByCreatedAtDesc();

    List<NoticePost> findAllByShelterIdOrderByCreatedAtDesc(Long shelterId);


    @Query(value = """
    SELECT 
        n.id AS id,
        n.title AS title,
        n.content AS content,
        n.view_count AS viewCount,
        TO_CHAR(n.created_at, 'YYYY-MM-DD HH24:MI:SS') AS createdAt,
        m.email AS email,
        m.nickname AS nickname,
        (SELECT COUNT(*) FROM comment c WHERE c.post_type = 'FREE' AND c.post_id = n.id) AS commentCount,
        (SELECT COUNT(*) FROM post_like l WHERE l.free_post_id = n.id) AS likeCount,
        (
                SELECT pi.image_url
                FROM post_image pi
                WHERE pi.free_post_id = n.id
                AND pi.image_url IS NOT NULL
                ORDER BY pi.id
                LIMIT 1
           ) AS imageUrl
    FROM notice_post n
    JOIN member m ON n.writer_id = m.id
    LEFT JOIN post_image pi ON pi.free_post_id = n.id
    GROUP BY n.id, n.title, n.content, n.view_count, n.created_at, m.email, m.nickname
    ORDER BY n.created_at DESC
""", nativeQuery = true)
    List<MemberNoticePostResDTO> findAllProjected();
}
