package org.project.heredoggy.user.adoption.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.project.heredoggy.domain.postgresql.adoption.FamilyAgreement;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdoptionSurveyRequestDTO {
    @NotNull(message = "옵션을 입력해 주세요.")
    private Boolean hasPetExp; // 반려동물 경험 여부
    private String petExpDetails; // 반려동물 경험 상세 설명

    @NotNull(message = "옵션을 입력해 주세요.")
    private Boolean hasPetNow; // 현재 반려중인 동물 여부
    private String petNowDetail; // 현재 반려동물의 정보

    @NotBlank(message = "가족 구성원을 입력해 주세요.")
    private String family;

    @NotNull(message = "가족 동의 정도를 입력해 주세요.")
    private FamilyAgreement familyAgreement;

    @NotBlank(message = "입양 사유를 입력해 주세요.")
    private String reason;

    @NotNull(message = "사진 공유 동의 여부를 입력해 주세요.")
    private Boolean sharePhoto;

    @NotNull(message = "책임 동의 여부를 입력해 주세요.")
    private Boolean commitEndOfLife;

    @NotNull(message = "입양 후 관리 동의 여부를 입력해 주세요.")
    private Boolean careAfterAdopt;

    @NotNull(message = "중성화 동의 여부를 입력해 주세요.")
    private Boolean agreeNeutering;

    @NotNull(message = "입양 비용 동의 여부를 입력해 주세요.")
    private Boolean agreeFee;

}
