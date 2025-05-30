package org.project.heredoggy.shelter.shelter.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SheltersResponseDTO {
    private Long id;
    private String shelterName;
    private String address;
    private String phone;
}
