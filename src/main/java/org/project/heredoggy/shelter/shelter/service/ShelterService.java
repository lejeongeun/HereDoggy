package org.project.heredoggy.shelter.shelter.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterImage;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterImageRepository;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.image.ImageService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.shelter.dto.SheltersDetailResponseDTO;
import org.project.heredoggy.shelter.shelter.dto.SheltersResponseDTO;
import org.project.heredoggy.shelter.shelterAdmin.dto.ShelterEditRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  ShelterService {
    private final ShelterRepository shelterRepository;
    private final ShelterImageRepository shelterImageRepository;
    private final ImageService imageService;

    public List<SheltersResponseDTO> getSheltersByRegion(String region) {
        List<Shelter> shelters = (region == null || region.isBlank()) ?
                shelterRepository.findAll() :
                shelterRepository.findByRegionContaining(region);

        return shelters.stream()
                .map(shelter -> SheltersResponseDTO.builder()
                        .id(shelter.getId())
                        .shelterName(shelter.getName())
                        .phone(shelter.getPhone())
                        .address(shelter.getAddress())
                        .build())
                .collect(Collectors.toList());
    }

    public SheltersDetailResponseDTO getShelterDetail(Long shelterId) {
        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(()-> new NotFoundException("보호소를 찾을 수 없습니다."));

        List<String> images = shelterImageRepository.findAllByShelterId(shelterId).stream()
                .map(ShelterImage::getImageUrl)
                .toList();

        return SheltersDetailResponseDTO.builder()
                .id(shelter.getId())
                .shelterName(shelter.getName())
                .description(shelter.getDescription())
                .phone(shelter.getPhone())
                .address(shelter.getAddress())
                .imagesUrls(images)
                .build();
    }

    public void editShelter(CustomUserDetails userDetails, ShelterEditRequestDTO request, List<MultipartFile> files) {
        Shelter shelter = shelterRepository.findByShelterAdmin(userDetails.getMember())
                .orElseThrow(() -> new NotFoundException("보호소를 찾을 수 없습니다."));

        String fullAddress = String.format("(%s) %s %s", request.getZipcode(), request.getAddress1(), request.getAddress2());

        shelter.setDescription(request.getDescription());
        shelter.setAddress(fullAddress);
        shelter.setPhone(request.getPhone());
        shelterRepository.save(shelter);

        if (request.getDeleteImageIds() != null && !request.getDeleteImageIds().isEmpty()) {
            for (Long imageId : request.getDeleteImageIds()) {
                ShelterImage image = shelterImageRepository.findById(imageId)
                        .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));
                if (!image.getShelter().equals(shelter)) {
                    throw new IllegalArgumentException("해당 보호소의 이미지가 아닙니다.");
                }

                imageService.deleteImage(image.getImageUrl());
                shelterImageRepository.delete(image);
            }
        }

        // 새 이미지가 있다면 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                try {
                    String imageUrl = imageService.saveShelterImage(file, shelter.getId());
                    ShelterImage image = ShelterImage.builder()
                            .imageUrl(imageUrl)
                            .shelter(shelter)
                            .build();
                    shelterImageRepository.save(image);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
                }
            }
        }

    }
}
