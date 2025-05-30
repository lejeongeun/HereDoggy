package org.project.heredoggy.user.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordResetConfirmDTO {
    private String token;
    private String newPassword;
    private String newPasswordChk;
}
