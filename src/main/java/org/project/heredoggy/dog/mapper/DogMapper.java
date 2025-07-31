package org.project.heredoggy.dog.mapper;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogImageResponseDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.dog.dto.MainDogResponseDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DogMapper {
    // 빌더 메소드 분리
    public DogResponseDTO toDto(Dog dog) {
        return DogResponseDTO.builder()
                .id(dog.getId())
                .name(dog.getName())
                .age(dog.getAge())
                .gender(dog.getGender())
                .personality(dog.getPersonality())
                .weight(dog.getWeight())
                .isNeutered(dog.getIsNeutered())
                .status(dog.getStatus())
                .foundLocation(dog.getFoundLocation())
                .images(dog.getImages().stream()
                        .map(img -> DogImageResponseDTO.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public MainDogResponseDTO toMainDog(Dog dog){
        return MainDogResponseDTO.builder()
                .id(dog.getId())
                .name(dog.getName())
                .age(dog.getAge())
                .gender(dog.getGender())
                .weight(dog.getWeight())
                .isNeutered(dog.getIsNeutered())
                .foundLocation(dog.getFoundLocation())
                .status(dog.getStatus())
                .imagesUrls(dog.getImages().stream()
                        .map(DogImage::getImageUrl)
                        .collect(Collectors.toList()))
                .shelterName(dog.getShelter().getName())
                .viewCount(dog.getViewCount())
                .build();
    }
}
