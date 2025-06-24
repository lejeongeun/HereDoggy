package org.project.heredoggy.match.peopensity.dto;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedBreedDTO {
    private Long breedId;
    private String breedName;
    private String imageUrl;
    private String description; // 강아지의 설명
    private String caution;
    private int score;
    private String reason; // 점수별 조건 충족 항목 스크립스 생성 = 추천 이유
}
