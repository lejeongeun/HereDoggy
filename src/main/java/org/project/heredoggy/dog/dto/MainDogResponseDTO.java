package org.project.heredoggy.dog.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.dog.DogStatus;
import org.project.heredoggy.domain.postgresql.dog.Gender;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainDogResponseDTO {
    private Long id;
    private String name;
    private int age;
    private Gender gender;
    private String personality;
    private Double weight;
    private Boolean isNeutered;
    private DogStatus status;
    private String foundLocation;
    private List<String> imagesUrls;
    private String shelterName;
}
