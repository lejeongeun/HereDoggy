package org.project.heredoggy.dog.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.*;
import org.project.heredoggy.dog.mapper.DogResponseMapper;
import org.project.heredoggy.domain.postgresql.dog.*;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.global.error.ErrorMessages;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
        // 보호소 인증 처리
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

        // 이미지 파일이 존재할 경우 -> dogImage 생성 + 연관관계 설정
        //saveImages(imageFiles, dog, shelter);

        if (imageFiles != null && !imageFiles.isEmpty()){
            for (MultipartFile file : imageFiles) {
                String imageUrl = imageService.saveImage(file, shelter.getId(), dog.getId());
                DogImage image = DogImage.builder()
                        .imageUrl(imageUrl)
                        .dog(dog)
                        .build();

                byte[] featureVector = dogImageSimilarityService.extractFeatureVector(imageUrl);
                image.setFeatureVector(featureVector);
                dog.addImage(image);
            }
        }
        dogRepository.save(dog);
    }
    @Transactional
    public void edit(Long sheltersId, CustomUserDetails userDetails, Long dogsId, DogEditRequestDTO request, List<MultipartFile> newImages, List<Long> deleteImageIds) throws IOException{
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        Dog dog = dogRepository.findById(dogsId).orElseThrow(
                ()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND)
        );

        if (!dog.getShelter().getId().equals(shelter.getId())){
            throw new ForbiddenException(ErrorMessages.NOT_YOUR_DOG);
        }
        // Dog 정보 수정
        dog.updateDog(request);

        // 기존 이미지 삭제
        if (deleteImageIds != null && !deleteImageIds.isEmpty()){
            List<DogImage> imagesToRemove = dog.getImages().stream()
                    .filter(img -> deleteImageIds.contains(img.getId()))
                    .collect(Collectors.toList());

            for (DogImage image : imagesToRemove){
                imageService.deleteImage(image.getImageUrl()); // 실제 파일 삭제
                dog.removeImage(image);
            }
        }
        if (newImages != null && !newImages.isEmpty()){
            for (MultipartFile file : newImages) {
                String imageUrl = imageService.saveImage(file, shelter.getId(), dog.getId());
                DogImage image = DogImage.builder()
                        .imageUrl(imageUrl)
                        .dog(dog)
                        .build();
                byte[] featureVector = dogImageSimilarityService.extractFeatureVector(imageUrl);
                image.setFeatureVector(featureVector);
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

        Dog dog = dogRepository.findById(dogsId).orElseThrow(
                ()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND)
        );
        return dogResponseMapper.toDto(dog);
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

    public List<MainDogResponseDTO> getAllDogs() {
        return dogRepository.findAll().stream()
                .map(dogResponseMapper::toMainDog)
                .collect(Collectors.toList());
    }

    public MainDogResponseDTO getDetailsDogMain(Long dogsId) {
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        return dogResponseMapper.toMainDog(dog);
    }

    // 유저) 보호소 인증 없이 해당 보호소의 강아지 목록 조회
    @Transactional(readOnly = true)
    public List<DogResponseDTO> getAnotherShelterDogs(Long sheltersId) {
        Shelter shelter = shelterRepository.findById(sheltersId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.SHELTERS_NOT_FOUND));

        return dogRepository.findByShelterId(shelter.getId()).stream()
                .map(dogResponseMapper::toDto)
                .collect(Collectors.toList());

    }

    /**
     * 특정 강아지의 유사한 Top N 강아지 추출 (사용자가 관심 등록한 강아지의 특징 벡터를 기반으로 유사한 다른 강아지들을 찾아 반환)
     * @param favoriteDogId -> 사용자가 관심 등록한 강아지
     * @param limit -> 추천할 강아지의 최대 갯수
     * @return 유사 강아지 목록
     */
    @Transactional(readOnly = true)
    public List<MainDogResponseDTO> getTopSimilarDogs(Long favoriteDogId, int limit) {
        FavoriteDog favoriteDog = favoriteDogRepository.findById(favoriteDogId)
                .orElseThrow(()-> new NotFoundException("관심 동물을 등록하여 주세요"));
        // 관심 등록된 강아지의 실제 Dog entity
        Dog interestedDog = favoriteDog.getDog();
        if (interestedDog == null || interestedDog.getImages().isEmpty()){
            return List.of();
        }

        DogImage interestedDogMainImage = interestedDog.getImages().get(0);
        byte[] interestedDogFeatureVector = interestedDogMainImage.getFeatureVector();

        if (interestedDogFeatureVector == null){
            return List.of();
        }

        List<Dog> allDogs = dogRepository.findAllWithImages();

        return allDogs.stream()
                .filter(dog -> !dog.getId().equals(interestedDog.getId())) // 추천 대상에서 자신의 정보 제외
                .filter(dog -> dog.getImages() != null && !dog.getImages().isEmpty()) // 이미지가 없을 경우 추천 대상에서 제외
                .filter(dog -> dog.getImages().get(0).getFeatureVector() != null) // 첫번째 이미지에 특징 벡터가 없는 강아지 제외
                .map(dog -> {
                    // 현재 강아지의 첫번째 이미지 특징 벡터 추출
                    byte[] currentDogFeatureVector = dog.getImages().get(0).getFeatureVector();
                    // Dog 코사인 유사도 계산
                    double similarity = dogImageSimilarityService.calculateSimilarity(interestedDogFeatureVector, currentDogFeatureVector);
                    return new SimilarDogResult(dog, similarity);
                }).sorted(Comparator.comparingDouble(SimilarDogResult::similarity).reversed())
                .limit(limit)
                .map(similarDogResult -> dogResponseMapper.toMainDog(similarDogResult.dog()))
                .collect(Collectors.toList());

    }
    private record SimilarDogResult(Dog dog, double similarity){}

}
