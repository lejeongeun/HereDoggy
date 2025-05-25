package org.project.heredoggy.shelter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShelterCreateRequestDTO {
    @NotBlank(message = "보호소 이름를 적어주세요")
    private String shelterName;

    @NotBlank(message = "담당자 번호를 적어주세요")
    private String phone;

    @NotBlank(message = "보호소 설명을 적어주세요")
    private String description;

    @NotBlank(message = "우편번호를 골라주세요")
    private String zipcode;

    @NotBlank(message = "주소칸 입력해주세요")
    private String address1;

    @NotBlank(message = "주소칸 입력해주세요")
    private String address2;
}
