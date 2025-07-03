package org.project.heredoggy.dog.mapper;

import org.project.heredoggy.dog.dto.DogImageResponseDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.dog.dto.MainDogResponseDTO;
import org.project.heredoggy.dog.favoriteDog.dto.DogFavoriteResponseDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
// DTO 매핑 클래스

@Component
public class DogResponseMapper {
    public DogResponseDTO toDto(Dog dog) {
        return DogResponseDTO.builder()
                .id(dog.getId())
                .name(dog.getName())
                .age(dog.getAge())
                .breedType(dog.getBreed())
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

    public MainDogResponseDTO toMainDog(Dog dog) {
        return MainDogResponseDTO.builder()
                .id(dog.getId())
                .name(dog.getName())
                .age(dog.getAge())
                .breedType(dog.getBreed())
                .gender(dog.getGender())
                .personality(dog.getPersonality())
                .weight(dog.getWeight())
                .isNeutered(dog.getIsNeutered())
                .foundLocation(dog.getFoundLocation())
                .status(dog.getStatus())
                .imageUrl(dog.getImages().stream()
                        .map(DogImage::getImageUrl)
                        .findFirst()
                        .orElse(null))
                .shelterName(dog.getShelter().getName())
                .build();
    }

    public DogFavoriteResponseDTO toFavoriteResponse(Dog dog, List<Dog> similarDogs, String message) {
        return DogFavoriteResponseDTO.builder()
                .message(message)
                .dog(toDto(dog))
                .similarDogs(similarDogs.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}