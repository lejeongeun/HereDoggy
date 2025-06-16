package org.project.heredoggy.systemAdmin.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelterRequestResponseDTO {
    private Long requestId;
    private String shelterName;
    private String phone;
    private String address;
    private String description;
    private String region;
    private String email;
    private String shelterCode;
    private String shelterAdmin;
    private String status;
}
