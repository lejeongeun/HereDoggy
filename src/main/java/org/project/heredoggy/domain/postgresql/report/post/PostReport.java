package org.project.heredoggy.domain.postgresql.report.post;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.comment.PostType;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.report.ReportReason;
import org.project.heredoggy.domain.postgresql.report.ReportStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PostReport {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member reporter;

    private Long postId; // 실제 게시글의 ID만 저장 (어느 테이블인지와는 무관)

    @Enumerated(EnumType.STRING)
    private PostType postType; // FREE, MISSING, NOTICE, REVIEW

    @ManyToOne(fetch = FetchType.LAZY)
    private ReportReason reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.UNRESOLVED;

    private String adminMemo;

    private String postTitleSnapshot;
    private String postContentSnapshot;
    private String writerNicknameSnapshot;
    private boolean wasPostDeleted;


    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}