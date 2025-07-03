package org.project.heredoggy.dog.service;

import ai.djl.translate.TranslateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.dog.dto.*;
import org.project.heredoggy.dog.mapper.DogResponseMapper;
import org.project.heredoggy.domain.postgresql.dog.*;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.InternalServerException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DogService {
    private final DogRepository dogRepository;
    private final ImageService imageService;
    private final ShelterRepository shelterRepository;
    private final DogImageSimilarityService dogImageSimilarityService;
    private final FavoriteDogRepository favoriteDogRepository;
    private final DogResponseMapper dogResponseMapper;

    @Transactional
    public void create(Long sheltersId, CustomUserDetails userDetails, DogRequestDTO request, List<MultipartFile> imageFiles) throws IOException {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        Dog dog = Dog.builder()
                .name(request.getName())
                .age(request.getAge())
                .breed(request.getBreedType())
                .gender(request.getGender())
                .personality(request.getPersonality())
                .weight(request.getWeight())
                .isNeutered(request.getIsNeutered())
                .foundLocation(request.getFoundLocation())
                .status(request.getStatus())
                .shelter(shelter)
                .build();
        dog = dogRepository.save(dog);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                String imageUrl = imageService.saveImage(file, shelter.getId(), dog.getId());
                DogImage image = DogImage.builder()
                        .imageUrl(imageUrl)
                        .dog(dog)
                        .build();
                try{
                    byte[] featureVector = dogImageSimilarityService.extractFeatureVector(imageUrl);
                    image.setFeatureVector(featureVector);
                } catch (TranslateException e) {
                    throw new RuntimeException(e);
                }

                dog.addImage(image);
            }
        }
        dogRepository.save(dog);
    }

    @Transactional
    public void edit(Long sheltersId, CustomUserDetails userDetails, Long dogsId, DogEditRequestDTO request, List<MultipartFile> newImages, List<Long> deleteImageIds) throws IOException {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        if (!dog.getShelter().getId().equals(shelter.getId())) {
            throw new ForbiddenException(ErrorMessages.NOT_YOUR_DOG);
        }

        dog.updateDog(request);

        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            List<DogImage> imagesToRemove = dog.getImages().stream()
                    .filter(img -> deleteImageIds.contains(img.getId()))
                    .collect(Collectors.toList());
            for (DogImage image : imagesToRemove) {
                imageService.deleteImage(image.getImageUrl());
                dog.removeImage(image);
            }
        }

        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile file : newImages) {
                String imageUrl = imageService.saveImage(file, shelter.getId(), dog.getId());
                DogImage image = DogImage.builder()
                        .imageUrl(imageUrl)
                        .dog(dog)
                        .build();
                try {
                    byte[] featureVector = dogImageSimilarityService.extractFeatureVector(imageUrl);
                    image.setFeatureVector(featureVector);
                } catch (TranslateException e) {
                    log.error("이미지 특성 벡터 추출 실패", e);
                    throw new InternalServerException("이미지 분석 중 오류가 발생했습니다.");
                }
                dog.addImage(image);
            }
        }
        dogRepository.save(dog);
    }

    public List<DogResponseDTO> getDogsByShelter(CustomUserDetails userDetails, Long sheltersId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        return dogRepository.findByShelterId(shelter.getId()).stream()
                .map(dogResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    public DogResponseDTO getDetailsDog(CustomUserDetails userDetails, Long sheltersId, Long dogsId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        return dogResponseMapper.toDto(dog);
    }

    @Transactional
    public void delete(CustomUserDetails userDetails, Long sheltersId, Long dogsId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        SheltersAuthUtils.getValidMember(userDetails);

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        if (!dog.getShelter().getId().equals(shelter.getId())) {
            throw new ForbiddenException(ErrorMessages.NOT_YOUR_DOG);
        }

        dogRepository.delete(dog);
    }

    public List<MainDogResponseDTO> getAllDogs() {
        return dogRepository.findAll().stream()
                .map(dogResponseMapper::toMainDog)
                .collect(Collectors.toList());
    }

    public MainDogResponseDTO getDetailsDogMain(Long dogsId) {
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        return dogResponseMapper.toMainDog(dog);
    }

    public DogResponseDTO getFullDetailsDog(Long dogsId) {
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        return dogResponseMapper.toDto(dog);
    }

    @Transactional(readOnly = true)
    public List<DogResponseDTO> getAnotherShelterDogs(Long sheltersId) {
        Shelter shelter = shelterRepository.findById(sheltersId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.SHELTERS_NOT_FOUND));
        return dogRepository.findByShelterId(shelter.getId()).stream()
                .map(dogResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MainDogResponseDTO> getTopSimilarDogs(Long favoriteDogId, int limit) {
        FavoriteDog favoriteDog = favoriteDogRepository.findById(favoriteDogId)
                .orElseThrow(() -> new NotFoundException("관심 동물을 등록하여 주세요"));
        Dog interestedDog = favoriteDog.getDog();
        if (interestedDog == null || interestedDog.getImages().isEmpty()) {
            return List.of();
        }

        DogImage interestedDogMainImage = interestedDog.getImages().get(0);
        byte[] interestedDogFeatureVector = interestedDogMainImage.getFeatureVector();
        if (interestedDogFeatureVector == null) {
            return List.of();
        }

        List<Dog> allDogs = dogRepository.findAllWithImages();
        return allDogs.stream()
                .filter(dog -> !dog.getId().equals(interestedDog.getId()))
                .filter(dog -> dog.getImages() != null && !dog.getImages().isEmpty())
                .filter(dog -> dog.getImages().get(0).getFeatureVector() != null)
                .map(dog -> {
                    byte[] currentDogFeatureVector = dog.getImages().get(0).getFeatureVector();
                    double similarity = dogImageSimilarityService.calculateCosineSimilarity(interestedDogFeatureVector, currentDogFeatureVector);
                    return new SimilarDogResult(dog, similarity);
                })
                .sorted(Comparator.comparingDouble(SimilarDogResult::similarity).reversed())
                .limit(limit)
                .map(similarDogResult -> dogResponseMapper.toMainDog(similarDogResult.dog()))
                .collect(Collectors.toList());
    }

    private record SimilarDogResult(Dog dog, double similarity) {}
}