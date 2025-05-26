package org.project.heredoggy.dog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Gender gender;
    private String personality;
    private Double weight;
    private Boolean isNeutered;
    private String foundLocation;
    private DogStatus status;


    // 삭제할 이미지(기존 이미지 목록)
    private List<String> imagesToDelete;
}
