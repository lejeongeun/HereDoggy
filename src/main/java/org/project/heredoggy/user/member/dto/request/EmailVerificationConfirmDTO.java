package org.project.heredoggy.user.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailVerificationConfirmDTO {
    private String email;
    private String code;
}