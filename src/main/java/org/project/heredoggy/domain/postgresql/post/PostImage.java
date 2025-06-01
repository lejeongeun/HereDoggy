package org.project.heredoggy.domain.postgresql.post;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.notice.NoticePost;
import org.project.heredoggy.domain.postgresql.post.free.FreePost;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPost;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPost;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "free_post_id", nullable = true)
    private FreePost freePost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_post_id", nullable = true)
    private ReviewPost reviewPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missing_post_id", nullable = true)
    private MissingPost missingPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_post_id", nullable = true)
    private NoticePost noticePost;
}
