package org.project.heredoggy.dog.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogEditRequestDTO;
import org.project.heredoggy.dog.dto.DogRequestDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.dog.exception.ErrorMessages;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.ImageUploadException;
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

    @Transactional
    public void create(Long sheltersId, CustomUserDetails userDetails, DogRequestDTO request, List<MultipartFile> imageFiles) throws IOException {
        // 보호소 인증 처리
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        List<DogImage> dogImages = imageFiles.stream()
                .map(file -> {
                    try{
                        String url = imageService.saveImage(file);
                        return DogImage.builder().imageUrl(url).build();
                    }catch (IOException e){
                        throw new RuntimeException("이미지 저장 실패", e);
                    }
                }).collect(Collectors.toList());

        Dog dog = Dog.builder()
                .name(request.getName())
                .age(request.getAge())
                .gender(request.getGender())
                .personality(request.getPersonality())
                .weight(request.getWeight())
                .isNeutered(request.getIsNeutered())
                .foundLocation(request.getFoundLocation())
                .status(request.getStatus())
                .images(dogImages)
                .shelter(shelter)
                .build();

        dogRepository.save(dog);
    }


    public List<DogResponseDTO> getDogsByShelter(Long sheltersId) {
        return dogRepository.findByShelterId(sheltersId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private DogResponseDTO toDto(Dog dog) {
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


    public DogResponseDTO getDetailsDog(Long dogsId) {
        Dog dog = dogRepository.findById(dogsId).orElseThrow(
                ()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND)
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
    @Transactional
    public void edit(Long sheltersId, CustomUserDetails userDetails,Long dogsId, DogEditRequestDTO request, List<MultipartFile> newImagesFiles) throws IOException{
        // shelterId 검증
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        // dog 검증
        Dog dog = dogRepository.findById(dogsId).orElseThrow(
                ()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND)
        );

        if (!dog.getShelter().getId().equals(shelter.getId())){
            throw new ForbiddenException(ErrorMessages.NOT_YOUR_DOG);
        }

        // 삭제 요청한 이미지의 url 목록 생성, 없을 경우 빈 리스트로 초기화
        List<String> toDelete = request.getImagesToDelete() != null ? request.getImagesToDelete() : List.of();

        // 삭제 대상이 아닌 이미지들만 필터링 하여 넘김
        List<DogImage> filteredImages = dog.getImages().stream()
                        .filter(image -> {
                            // 삭제 대상인지 확인
                            boolean shouldDelete = toDelete.contains(image.getImageUrl());
                            if (shouldDelete){
                                imageService.deleteImage(image.getImageUrl());
                            }
                            return !shouldDelete;
                        }).collect(Collectors.toList());
        // 새로 업로드할 파일이 있는 경우
        if (newImagesFiles != null && !newImagesFiles.isEmpty()){
            // MultipartFile 리스트를 DogImage 객체로 변환
            List<DogImage> newImages = newImagesFiles.stream()
                    .map(file -> {
                        try{
                            String url = imageService.saveImage(file);
                            return DogImage.builder().imageUrl(url).build();
                        }catch (IOException e){
                            throw new ImageUploadException("이미지 저장 실패", e);
                        }
                    }).collect(Collectors.toList());
            filteredImages.addAll(newImages);
        }

        dog.setName(request.getName());
        dog.setAge(request.getAge());
        dog.setGender(request.getGender());
        dog.setPersonality(request.getPersonality());
        dog.setWeight(request.getWeight());
        dog.setIsNeutered(request.getIsNeutered());
        dog.setFoundLocation(request.getFoundLocation());
        dog.setStatus(request.getStatus());
        dog.setImages(filteredImages);

        dogRepository.save(dog);

    }
    @Transactional
    public void delete(Long sheltersId, CustomUserDetails userDetails, Long dogsId) {
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
}
