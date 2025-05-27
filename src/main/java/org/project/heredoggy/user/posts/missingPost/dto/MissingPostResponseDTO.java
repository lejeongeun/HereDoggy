package org.project.heredoggy.user.posts.missingPost.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.post.missing.DogGender;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPostType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissingPostResponseDTO {
    private Long id;
    private String title; //제목

    private MissingPostType type; //실종, 발견

    private DogGender gender; //성별
    private Integer age; //나이
    private Double weight; //몸무게
    private String furColor; //털 색상

    private String feature; //특징

    private String missingDate;//실종(목격) 날짜
    private String missingLocation; //실종(목격)장소

    private String description; //설명

    private Boolean isContactPublic; //연락처 공개여부
}
