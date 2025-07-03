package org.project.heredoggy.dog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project.heredoggy.domain.postgresql.dog.BreedType;
import org.project.heredoggy.domain.postgresql.dog.DogStatus;
import org.project.heredoggy.domain.postgresql.dog.Gender;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DogEditRequestDTO {
    private String name;
    private int age;
    private BreedType breedType;
    private Gender gender;
    private String personality;
    private Boolean isNeutered;
    private String foundLocation;
    private DogStatus status;

}
