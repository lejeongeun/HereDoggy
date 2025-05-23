package org.project.heredoggy.dog.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogRequestDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogService {
    private final DogRepository dogRepository;

    public void create(DogRequestDTO request) {
        Dog dog = Dog.builder()
                .name(request.getName())
                .age(request.getAge())
                .gender(request.getGender())
                .personality(request.getPersonality())
                .weight(request.getWeight())
                .isNeutered(request.getIsNeutered())
                .foundLocation(request.getFoundLocation())
                .images(request.getImagesUrls().stream()
                        .map(url -> DogImage.builder().imageUrl(url).build())
                        .collect(Collectors.toList())
                ).build();
    }
}
