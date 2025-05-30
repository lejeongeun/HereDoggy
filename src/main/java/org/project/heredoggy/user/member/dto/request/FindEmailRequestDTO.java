package org.project.heredoggy.user.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindEmailRequestDTO {
    private String name;
    private String phone;
}