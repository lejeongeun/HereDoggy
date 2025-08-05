package org.project.heredoggy.dog.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.*;
import org.project.heredoggy.dog.mapper.DogMapper;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final ImageService imageService;
    private final ShelterRepository shelterRepository;
    private final DogMapper dogMapper;
    private final DogCacheService dogCacheService;

    @Transactional
    public void create(Long sheltersId, CustomUserDetails userDetails, DogRequestDTO request, List<MultipartFile> imageFiles) throws IOException {
        // 보호소 인증 처리
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        Dog dog = Dog.builder()
                .name(request.getName())
                .age(request.getAge())
                .gender(request.getGender())
                .personality(request.getPersonality())
                .weight(request.getWeight())
                .isNeutered(request.getIsNeutered())
                .foundLocation(request.getFoundLocation())
                .status(request.getStatus())
                .shelter(shelter)
                .build();
        dog = dogRepository.save(dog);

        // 이미지 파일이 존재할 경우 -> dogImage 생성 + 연관관계 설정
        saveImages(imageFiles, dog, shelter);
    }

    @Transactional(readOnly = true)
    public List<DogResponseDTO> getDogsByShelter(CustomUserDetails userDetails, Long sheltersId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        return dogRepository.findByShelterId(shelter.getId()).stream()
                .map(dogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DogResponseDTO getDetailsDog(CustomUserDetails userDetails, Long sheltersId, Long dogsId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        Dog dog = dogRepository.findById(dogsId).orElseThrow(
                ()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND)
        );
        return dogMapper.toDto(dog);
    }

    @Transactional
    public void edit(Long sheltersId, CustomUserDetails userDetails,Long dogsId, DogEditRequestDTO request, List<MultipartFile> newImages, List<Long> deleteImageIds) throws IOException{
        // shelterId 검증
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        // dog 검증
        Dog dog = dogRepository.findById(dogsId).orElseThrow(
                ()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND)
        );

        if (!dog.getShelter().getId().equals(shelter.getId())){
            throw new ForbiddenException(ErrorMessages.NOT_YOUR_DOG);
        }

        // Dog 정보 수정
        dog.setName(request.getName());
        dog.setAge(request.getAge());
        dog.setGender(request.getGender());
        dog.setPersonality(request.getPersonality());
        dog.setWeight(request.getWeight());
        dog.setIsNeutered(request.getIsNeutered());
        dog.setFoundLocation(request.getFoundLocation());
        dog.setStatus(request.getStatus());

        // 기존 이미지 삭제
        if (deleteImageIds != null && !deleteImageIds.isEmpty()){
            List<DogImage> imagesToRemove = dog.getImages().stream()
                    .filter(img -> deleteImageIds.contains(img.getId()))
                    .collect(Collectors.toList());

            for (DogImage image : imagesToRemove){
                imageService.deleteImage(image.getImageUrl()); // 실제 파일 삭제
                image.setDog(null);
            }
            dog.getImages().removeAll(imagesToRemove);
        }
        saveImages(newImages, dog, shelter);

        dogRepository.save(dog);
    }

    @Transactional
    public void delete(CustomUserDetails userDetails, Long sheltersId, Long dogsId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        // 로그인 검증
        SheltersAuthUtils.getValidMember(userDetails);

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND)
        );
        if (!dog.getShelter().getId().equals(shelter.getId())){
            throw new ForbiddenException(ErrorMessages.NOT_YOUR_DOG);
        }

        dogRepository.delete(dog);
    }

    @Transactional(readOnly = true)
    public List<MainDogResponseDTO> getAllDogs() {
        return dogRepository.findAll().stream()
                .map(dogMapper::toMainDog)
                .collect(Collectors.toList());
    }

    @Transactional
    public MainDogResponseDTO getDetailsDogMain(Long dogsId) {
        MainDogResponseDTO cached = dogCacheService.getDogDetailFromCache(dogsId);

        if (cached != null){
            return cached;
        }

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        dog.increaseViewCount();

        MainDogResponseDTO dto = dogMapper.toMainDog(dog);

        dogCacheService.setDogDetailToCache(dogsId, dto);

        return dto;
    }

    private void saveImages (List<MultipartFile> imageFiles, Dog dog, Shelter shelter) throws IOException{
        if (imageFiles != null && !imageFiles.isEmpty()){
            for (MultipartFile file : imageFiles){
                String imageUrl = imageService.saveImage(file, shelter.getId(), dog.getId()); // 이미지 저장 후 url 반환
                DogImage image = DogImage.builder()
                        .imageUrl(imageUrl)
                        .build();
                dog.addImage(image);
            }
        }
    }

    // 유저) 보호소 인증 없이 해당 보호소의 강아지 목록 조회
    public List<DogResponseDTO> getAnotherShelterDogs(Long sheltersId) {
        Shelter shelter = shelterRepository.findById(sheltersId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.SHELTERS_NOT_FOUND));

        return dogRepository.findByShelterId(shelter.getId()).stream()
                .map(dogMapper::toDto)
                .collect(Collectors.toList());
    }
}
