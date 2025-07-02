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

    // 사용자의 기본 정보 주거 형태 (아파트 / 주택)
    @Enumerated(EnumType.STRING)
    private HomeType homeType;

    // 1. 사용자가 선호하는 강아지 크기 DogBreed의 DogSize와 매칭
    @Enumerated(EnumType.STRING)
    private DogSize size;

    // 2. 털 빠짐에 대한 민감도 DogBreed의 hairLoss와 매칭
    @Enumerated(EnumType.STRING)
    private Sensitivity hairLossSensitivity;

    // 3. 하루 평균 산책 가능 시간 DogBreed의 ExerciseNeed와 매칭
    @Enumerated(EnumType.STRING)
    private ExerciseTime exerciseTime;

    // 4. 보호자가 집을 비우는 시간 DogBreed의 Independence와 매칭
    @Enumerated(EnumType.STRING)
    private AloneTime aloneTime;

    // 5. 털 손질 가능 여부 DogBreed의 GroomingNeed와 매칭
    @Enumerated(EnumType.STRING)
    private GroomingWillingness groomingWillingness;

    // 6. 훈련 가능 여부 (쉽게 or 천천히) DogBreed의 Trainability와 매칭
    @Enumerated(EnumType.STRING)
    private TrainingEffort trainingEffort;

    // 7. 보호자의 활동성 (집순이 / 활동적) DogBreed의 ActivityDemand와 매칭
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    // 8. 병원비에 대한 부담 수준 DogBreed의 MedicalCost와 매칭
    @Enumerated(EnumType.STRING)
    private MedicalBudget medicalBudget;

    // 9. 아이가 집에 있는지 여부 DogBreed의 kidFriendly와 매칭 (true면 아이와 잘 지내는 성향)
    private boolean kidsInHouse;

    // 10. 알레르기 걱정 여부 (DogBreed의 hypoallergenic와 매칭 true면 저 알레르기 품종이므로 잘 맞음)
    private boolean allergyConcern;

    // 11. 반려견을 처음 키우는지 여부 (DogBreed의 goodForFirstTimeOwner와 매칭 true면 초보자도 쉽게 관리 가능)
    private boolean firstTimeOwner;


    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;
}
