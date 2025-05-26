package org.project.heredoggy.dog.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogEditRequestDTO;
import org.project.heredoggy.dog.dto.DogRequestDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.image.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final ImageService imageService;

    public void create(DogRequestDTO request, List<MultipartFile> imageFiles)throws IOException {
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
                .build();

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

    public void edit(Long id, DogEditRequestDTO request, List<MultipartFile> newImagesFiles) throws IOException{
        Dog dog = dogRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("정보가 존재하지 않습니다. 다시 확인하여 주세요.")
        );

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
                            throw new RuntimeException("이미지 저장 실패", e);
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

    public void delete(Long id) {
        Dog dog = dogRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("정보가 존재하지 않습니다. 다시 확인하여 주세요.")
        );
        dogRepository.delete(dog);
    }
}
