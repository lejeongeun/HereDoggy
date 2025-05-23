package org.project.heredoggy.dog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.project.heredoggy.domain.postgresql.dog.Gender;

import java.util.List;

@Getter
@NoArgsConstructor
public class DogRequestDTO {
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @Min(value = 0, message = "나이를 설정해 주세요.")
    private int age;

    @NotBlank(message = "성별 입력해 주세요.")
    private Gender gender;

    private String personality; // 특이사항

    @NotNull(message = "무게를 입력해 주세요.")
    private Double weight;

    @NotNull(message = "중성화 여부를 입력해 주세요")
    private Boolean isNeutered;

    private String foundLocation;

    @Size(min = 1, max = 5, message = "이미지는 1장 이상 5장 이하로 등록해 주세요.")
    private List<String> imagesUrls;

}
