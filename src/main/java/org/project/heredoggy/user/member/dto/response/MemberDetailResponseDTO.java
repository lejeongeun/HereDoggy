package org.project.heredoggy.user.member.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class MemberDetailResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private LocalDate birth;
    private String phone;
    private String address;
    private String profileImageUrl;
    private Boolean isNotificationEnabled;
    private Boolean isActive;
    private String role;
    private Double totalWalkDistance;
    private Long totalWalkDuration;
    private LocalDateTime createdAt;
}