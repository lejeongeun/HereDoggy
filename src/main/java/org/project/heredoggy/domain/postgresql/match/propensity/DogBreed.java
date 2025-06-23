package org.project.heredoggy.domain.postgresql.match.propensity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogBreed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 품종명 (예: 시추, 푸들 등)
    private String breedName;

    // 견종의 크기
    @Enumerated(EnumType.STRING)
    private DogSize size;

    // 털 빠짐 정도
    @Enumerated(EnumType.STRING)
    private HairLoss hairLoss;

    // 운동량 필요 정도
    @Enumerated(EnumType.STRING)
    private ExerciseNeed exerciseNeed;

    // 아이들과 잘 지내는 성향
    private boolean kidFriendly;

    // 독립성 정도 (혼자 있는 시간 가능 여부)
    @Enumerated(EnumType.STRING)
    private Independence independence;

    // 알레르기 유발 가능성 (true면 저알레르기 품종)
    private boolean hypoallergenic;

    // 인기 있는 품종인지 여부
    private boolean popular;

    // 초보자에게 적합한지 여부
    private boolean goodForFirstTimeOwner;

    // 소음에 둔감한지 여부
    private boolean noiseTolerant;

    // 미용/털손질 필요 정도
    @Enumerated(EnumType.STRING)
    private GroomingNeed groomingNeed;

    // 훈련 난이도
    @Enumerated(EnumType.STRING)
    private Trainability trainability;

    // 활동성 요구 정도
    @Enumerated(EnumType.STRING)
    private ActivityDemand activityDemand;

    // 병원비 평균 수준
    @Enumerated(EnumType.STRING)
    private MedicalCost medicalCost;

    // 품종에 대한 설명 (장점 등)
    private String description;

    // 주의사항 (질병 등)
    private String caution;

    // 이미지 URL 또는 경로
    private String imageUrl;

}
