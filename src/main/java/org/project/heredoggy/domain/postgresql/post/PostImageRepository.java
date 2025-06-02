package org.project.heredoggy.domain.postgresql.post;

import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPost;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByFreePost(FreePost freePost);
    List<PostImage> findByReviewPost(ReviewPost reviewPost);
    List<PostImage> findByMissingPost(MissingPost missingPost);
    List<PostImage> findByNoticePost(NoticePost noticePost);

    long countByFreePost(FreePost freePost);
    long countByReviewPost(ReviewPost reviewPost);
    long countByMissingPost(MissingPost missingPost);
    long countByNoticePost(NoticePost noticePost);
}
