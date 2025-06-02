package org.project.heredoggy.user.adoption.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.adoption.FamilyAgreement;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdoptionSurveyResponseDTO {
    private Long id;
    private Long adoption_id;

    private Boolean hasPetExp; // 반려동물 경험 여부
    private String petExpDetails; // 반려동물 경험 상세 설명

    private Boolean hasPetNow; // 현재 반려중인 동물 여부
    private String petNowDetail; // 현재 반려동물의 정보

    private String family; // 가족 구성원
    private FamilyAgreement familyAgreement; // 가족 동의 수준

    private String reason; // 입양하고자 하는 이유

    private Boolean sharePhoto; // 사진 동의
    private Boolean commitEndOfLife; // 책임 여부
    private Boolean careAfterAdopt; // 시간/환경적 동의 여부
    private Boolean agreeNeutering; // 중성화 동의 여부
    private Boolean agreeFee; // 입양 비용에 대한 동의 여부

    private LocalDateTime createdAt;


}
