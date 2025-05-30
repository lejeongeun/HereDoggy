package org.project.heredoggy.shelter.shelter.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterImage;

import java.util.List;

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
    private List<String> imagesUrls;
}
