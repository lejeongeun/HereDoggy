package org.project.heredoggy.shelter.shelter.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.shelter.shelter.dto.SheltersDetailResponseDTO;
import org.project.heredoggy.shelter.shelter.dto.SheltersResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelterService {
    private final ShelterRepository shelterRepository;
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

        return SheltersDetailResponseDTO.builder()
                .id(shelter.getId())
                .shelterName(shelter.getName())
                .description(shelter.getDescription())
                .phone(shelter.getPhone())
                .address(shelter.getAddress())
                .build();
    }
}
