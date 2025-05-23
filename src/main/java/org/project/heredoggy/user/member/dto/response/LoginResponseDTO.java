package org.project.heredoggy.user.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
}