package org.project.heredoggy.domain.postgresql.post.like;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPost;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPost;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "post_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "free_post_id")
    private FreePost freePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missing_post_id")
    private MissingPost missingPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_post_id")
    private ReviewPost reviewPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_post_id")
    private NoticePost noticePost;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
