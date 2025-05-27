package org.project.heredoggy.user.posts.missingPost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.heredoggy.domain.postgresql.post.missing.DogGender;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPostType;

@Getter
@NoArgsConstructor
public class MissingPostRequestDTO {
    @NotBlank(message = "제목을 입력해주세요")
    private String title; //제목

    @NotNull(message = "분류 작업을 해주세요")
    private MissingPostType type; //실종, 발견

    @NotNull(message = "성별을 골라주세요")
    private DogGender gender; //성별

    private Integer age; //나이
    private Double weight; //몸무게
    private String furColor; //털 색상

    @NotBlank(message = "특징을 적어주세요")
    private String feature; //특징

    private String missingDate;//실종(목격) 날짜

    private String missingLocation; //실종(발견)장소

    private String description; //설명

    private Boolean isContactPublic; //연락처 공개여부


}
