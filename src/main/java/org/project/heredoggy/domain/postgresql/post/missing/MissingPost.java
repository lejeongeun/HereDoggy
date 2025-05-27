package org.project.heredoggy.domain.postgresql.post.missing;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class MissingPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MissingPostType type;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private DogGender gender;

    private Integer age;

    private Double weight;

    private String furColor;

    private String feature;

    private LocalDate missingDate;

    private String missingLocation;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean isContactPublic;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long viewCount;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;
}
