package org.project.heredoggy.domain.postgresql.post.like;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPost;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberAndFreePost(Member member, FreePost freePost);
    boolean existsByMemberAndMissingPost(Member member, MissingPost missingPost);

    boolean existsByMemberAndReviewPost(Member member, ReviewPost reviewPost);
    boolean existsByMemberAndNoticePost(Member member, NoticePost noticePost);

    void deleteByMemberAndFreePost(Member member, FreePost freePost);
    void deleteByMemberAndMissingPost(Member member, MissingPost missingPost);
    void deleteByMemberAndReviewPost(Member member, ReviewPost reviewPost);
    void deleteByMemberAndNoticePost(Member member, NoticePost noticePost);

    Long countByFreePost(FreePost freePost);
    Long countByMissingPost(MissingPost missingPost);
    Long countByReviewPost(ReviewPost reviewPost);
    Long countByNoticePost(NoticePost noticePost);

    @Query("SELECT l.freePost.id AS postId, COUNT(1) AS count " + "FROM Like l WHERE l.freePost IN :posts GROUP BY l.freePost.id")
    List<LikeCountProjection> countLikesByFreePostIn(@Param("posts") List<FreePost> posts);

    public interface LikeCountProjection {
        Long getPostId();
        Long getCount();
    }
}
