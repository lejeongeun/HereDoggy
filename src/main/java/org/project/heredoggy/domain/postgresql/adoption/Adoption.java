package org.project.heredoggy.domain.postgresql.adoption;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Adoption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isMarried = false;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status = AdoptionStatus.PENDING;

    private LocalDate visitDate; // 방문 날짜

    private LocalTime visitTime;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "decision_at")
    private LocalDateTime decisionAt; // 관리자 승인 시간

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

    @OneToOne(mappedBy = "adoption", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdoptionSurvey survey;
}
