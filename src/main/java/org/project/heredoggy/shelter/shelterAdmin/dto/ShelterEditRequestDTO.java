package org.project.heredoggy.shelter.shelterAdmin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ShelterEditRequestDTO {
    private String phone;
    private String description;
    private String zipcode;
    private String address1;
    private String address2;
    private List<Long> deleteImageIds;
}



