package org.project.heredoggy.domain.postgresql.notice;

import org.project.heredoggy.shelter.noticePost.dto.NoticePostResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticePostRepository extends JpaRepository<NoticePost, Long> {
    @Query(value = """
    SELECT np.id AS id,
           np.title AS title,
           np.content AS content,
           np.view_count AS viewCount,
           TO_CHAR(np.created_at, 'YYYY-MM-DD HH24:MI:SS') AS createdAt,
           m.email AS email,
           m.nickname AS nickname,
           (
               SELECT COUNT(*) 
               FROM comment c 
               WHERE c.post_type = 'NOTICE' 
               AND c.post_id = np.id
           ) AS commentCount,
           (
               SELECT COUNT(*) 
               FROM post_like pl 
               WHERE pl.notice_post_id = np.id
           ) AS likeCount,
           (
               SELECT pi.image_url
               FROM post_image pi
               WHERE pi.notice_post_id = np.id
               AND pi.image_url IS NOT NULL
               ORDER BY pi.id
               LIMIT 1
           ) AS imageUrl
    FROM notice_post np
    JOIN member m ON np.writer_id = m.id
    WHERE np.shelter_id = :shelterId
    ORDER BY np.created_at DESC
""", nativeQuery = true)
    List<NoticePostResDTO> findAllByShelterIdProjected(@Param("shelterId") Long shelterId);

}
