package org.project.heredoggy.systemAdmin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminEditRequestDTO {
    @NotBlank(message = "패스워드를 입력하세요")
    private String password;

    @NotBlank(message = "패스워드를 재입력하세요")
    private String passwordCheck;

    @NotBlank(message = "본인 이름을 입력하세요")
    private String name;

    @NotBlank(message = "닉네임을 입력하세요")
    private String nickname;

    @NotBlank(message = "본인 전화번호를 입력하세요")
    private String phone;

    @NotBlank(message = "우편번호를 골라주세요")
    private String zipcode;

    @NotBlank(message = "주소칸 입력해주세요")
    private String address1;

    @NotBlank(message = "주소칸 입력해주세요")
    private String address2;
}
