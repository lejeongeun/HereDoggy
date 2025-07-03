package org.project.heredoggy.dog.favoriteDog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.dog.dto.DogResponseDTO;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogFavoriteResponseDTO {
    private String message; // 응답 메시지 (예: "관심 동물이 등록되었습니다.")
    private DogResponseDTO dog; // 등록된 강아지 (다중 이미지)
    private List<DogResponseDTO> similarDogs; // 유사견 목록 (다중 이미지)
}