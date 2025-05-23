package org.project.heredoggy.dog.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogRequestDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .status(request.getStatus())
                .images(request.getImagesUrls().stream()
                        .map(url -> DogImage.builder().imageUrl(url).build())
                        .collect(Collectors.toList())
                ).build();

        dogRepository.save(dog);
    }


    public List<DogResponseDTO> getAllDog() {
        return dogRepository.findAll().stream()
                .map(dog -> DogResponseDTO.builder()
                        .name(dog.getName())
                        .age(dog.getAge())
                        .gender(dog.getGender())
                        .personality(dog.getPersonality())
                        .weight(dog.getWeight())
                        .isNeutered(dog.getIsNeutered())
                        .foundLocation(dog.getFoundLocation())
                        .imagesUrls(dog.getImages().stream()
                                .map(DogImage::getImageUrl)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }


    public DogResponseDTO getDetailsDog(Long id) {
        Dog dog = dogRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("정보가 존재하지 않습니다. 다시 확인하여 주세요.")
        );
        return DogResponseDTO.builder()
                .name(dog.getName())
                .age(dog.getAge())
                .gender(dog.getGender())
                .personality(dog.getPersonality())
                .weight(dog.getWeight())
                .isNeutered(dog.getIsNeutered())
                .foundLocation(dog.getFoundLocation())
                .imagesUrls(dog.getImages().stream()
                        .map(DogImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }

    public void edit(Long id, DogRequestDTO request) {
        Dog dog = dogRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("정보가 존재하지 않습니다. 다시 확인하여 주세요.")
        );
        dog.setName(request.getName());
        dog.setAge(request.getAge());
        dog.setGender(request.getGender());
        dog.setPersonality(request.getPersonality());
        dog.setWeight(request.getWeight());
        dog.setIsNeutered(request.getIsNeutered());
        dog.setFoundLocation(request.getFoundLocation());
        dog.setStatus(request.getStatus());
        dog.setImages(request.getImagesUrls().stream()
                .map(url -> DogImage.builder().imageUrl(url).build())
                .collect(Collectors.toList()));
        dogRepository.save(dog);

    }

    public void delete(Long id) {
        Dog dog = dogRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("정보가 존재하지 않습니다. 다시 확인하여 주세요.")
        );
        dogRepository.delete(dog);
    }
}
