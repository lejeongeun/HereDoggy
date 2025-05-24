package org.project.heredoggy.systemAdmin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class AdminProfileResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private LocalDate birth;
    private String phone;
    private String address;
    private String profileImageUrl;
    private Boolean isActive;
    private String role;
    private LocalDateTime createdAt;
}