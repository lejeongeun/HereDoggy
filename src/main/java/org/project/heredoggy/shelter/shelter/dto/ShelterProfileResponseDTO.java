package org.project.heredoggy.shelter.shelter.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShelterProfileResponseDTO {
    private Long shelterId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String region;
    private String description;
    private String shelterCode;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}
