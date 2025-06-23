package org.project.heredoggy.domain.postgresql.match.propensity;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 인기 견종을 선호하는지 여부
    private boolean popularity;

    // 반려견을 처음 키우는지 여부
    private boolean firstTimeOwner;

    // 이웃 소음에 민감한지 여부
    private boolean noiseTolerance;

    // 알레르기 걱정 여부
    private boolean allergyConcern;

    // 사용자가 선호하는 강아지 크기
    @Enumerated(EnumType.STRING)
    private DogSize size;

    // 털 빠짐에 대한 민감도
    @Enumerated(EnumType.STRING)
    private Sensitivity hairLossSensitivity;

    // 하루 평균 산책 시간
    @Enumerated(EnumType.STRING)
    private ExerciseTime exerciseTime;

    // 아이가 집에 있는지 여부
    private boolean kidsInHouse;

    // 짖음에 대한 민감도
    @Enumerated(EnumType.STRING)
    private Sensitivity barkingTolerance;

    // 보호자가 집을 비우는 시간
    @Enumerated(EnumType.STRING)
    private AloneTime aloneTime;

    // 주거 형태 (아파트 / 주택)
    @Enumerated(EnumType.STRING)
    private HomeType homeType;

    // 병원비에 대한 부담 수준
    @Enumerated(EnumType.STRING)
    private MedicalBudget medicalBudget;

    // 털 손질 가능 여부
    @Enumerated(EnumType.STRING)
    private GroomingWillingness groomingWillingness;

    // 훈련 가능 여부 (쉽게 or 천천히)
    @Enumerated(EnumType.STRING)
    private TrainingEffort trainingEffort;

    // 보호자의 활동성 (집순이 / 활동적)
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "surveyAnswer")
    private Member member;
}
