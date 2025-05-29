package org.project.heredoggy.shelter.shelter.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SheltersDetailResponseDTO {
    private Long id;
    private String shelterName;
    private String description;
    private String phone;
    private String address;
}
