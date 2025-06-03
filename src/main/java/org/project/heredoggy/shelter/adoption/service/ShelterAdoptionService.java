package org.project.heredoggy.shelter.adoption.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.adoption.Adoption;
import org.project.heredoggy.domain.postgresql.adoption.AdoptionRepository;
import org.project.heredoggy.domain.postgresql.adoption.AdoptionStatus;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.exception.UnauthorizedException;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.adoption.dto.AdoptionDetailsResponseDto;
import org.project.heredoggy.shelter.adoption.dto.AdoptionListResponseDTO;
import org.project.heredoggy.user.adoption.dto.AdoptionSurveyResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterAdoptionService {
    private final AdoptionRepository adoptionRepository;

    @Transactional(readOnly = true)
    public List<AdoptionListResponseDTO> getAllAdoptions(CustomUserDetails userDetails, Long sheltersId) {
        validateShelter(userDetails, sheltersId);
        List<Adoption> adoptionList = adoptionRepository.findAllByShelterId(sheltersId);

        return adoptionList.stream()
                .map(this::toAdoptionListDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public AdoptionDetailsResponseDto getDetailsAdoptions(CustomUserDetails userDetails, Long sheltersId, Long adoptionsId) {
        validateShelter(userDetails, sheltersId);
        Adoption adoption = adoptionRepository.findById(adoptionsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.ADOPTION_NOT_FOUND));
        return toAdoptionDetailsDto(adoption);
    }

    @Transactional
    public void approvedAdoptions(CustomUserDetails userDetails, Long sheltersId, Long adoptionsId) {
        Adoption adoption = adoptionRepository.findById(adoptionsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.ADOPTION_NOT_FOUND));
        validateShelter(userDetails, sheltersId);
        adoption.setStatus(AdoptionStatus.APPROVED);
        adoptionRepository.save(adoption);
    }
    @Transactional
    public void rejectAdoptions(CustomUserDetails userDetails, Long sheltersId, Long adoptionsId) {
        Adoption adoption = adoptionRepository.findById(adoptionsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.ADOPTION_NOT_FOUND));
        validateShelter(userDetails, sheltersId);
        adoption.setStatus(AdoptionStatus.REJECTED);
        adoptionRepository.save(adoption);
    }
    public void validateShelter(CustomUserDetails userDetails, Long sheltersId){
        SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
    }

    public AdoptionListResponseDTO toAdoptionListDto(Adoption adoption){
        return AdoptionListResponseDTO.builder()
                .adoptionId(adoption.getId())
                .memberId(adoption.getMember().getId())
                .memberName(adoption.getMember().getName())
                .memberPhone(adoption.getMember().getPhone())
                .dogId(adoption.getDog().getId())
                .dogName(adoption.getDog().getName())
                .visitDate(adoption.getVisitDate())
                .visitTime(adoption.getVisitTime())
                .createdAt(adoption.getCreatedAt())
                .status(adoption.getStatus())
                .decisionAt(adoption.getDecisionAt())
                .build();
    }

    public AdoptionDetailsResponseDto toAdoptionDetailsDto(Adoption adoption){
        return AdoptionDetailsResponseDto.builder()
                .adoptionId(adoption.getId())
                .memberId(adoption.getMember().getId())
                .memberName(adoption.getMember().getName())
                .memberPhone(adoption.getMember().getPhone())
                .dogId(adoption.getDog().getId())
                .dogName(adoption.getDog().getName())
                .visitDate(adoption.getVisitDate())
                .visitTime(adoption.getVisitTime())
                .createdAt(adoption.getCreatedAt())
                .status(adoption.getStatus())
                .decisionAt(adoption.getDecisionAt())
                .survey(AdoptionSurveyResponseDTO.surveyResponse(adoption.getSurvey())) // 설문지
                .build();
    }
}