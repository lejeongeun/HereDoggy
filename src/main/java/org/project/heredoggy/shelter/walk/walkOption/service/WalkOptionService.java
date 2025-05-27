package org.project.heredoggy.shelter.walk.walkOption.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.exception.ErrorMessages;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.walk.walkOption.WalkOption;
import org.project.heredoggy.domain.postgresql.walk.walkOption.WalkOptionRepository;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.walkOption.dto.WalkOptionRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WalkOptionService {
    private final WalkOptionRepository walkOptionRepository;
    private final DogRepository dogRepository;
    private final ErrorMessages errorMessages;

    @Transactional
    public void create(CustomUserDetails userDetails, Long sheltersId, Long dogsId, WalkOptionRequestDTO request) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(() -> new NotFoundException(errorMessages.DOG_NOT_FOUND));
        
        if (!dog.getShelter().getId().equals(shelter.getId())){
            throw new ForbiddenException(errorMessages.NOT_YOUR_DOG);
        }
        
        WalkOption walkOption = WalkOption.builder()
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .dog(dog)
                .build();

        walkOptionRepository.save(walkOption);
    }


    public void edit(CustomUserDetails userDetails, Long sheltersId, Long dogsId, WalkOptionRequestDTO request, Long optionsId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(() -> new NotFoundException(errorMessages.DOG_NOT_FOUND));
        
        if (!dog.getShelter().getId().equals(shelter.getId())){
            throw new ForbiddenException(errorMessages.NOT_YOUR_DOG);
        }
        WalkOption walkOption = walkOptionRepository.findById(optionsId)
                .orElseThrow(()-> new NotFoundException("해당 옵션 정보가 존재하지 않습니다."));

        walkOption.setDate(request.getDate());
        walkOption.setStartTime(request.getStartTime());
        walkOption.setEndTime(request.getEndTime());

        walkOptionRepository.save(walkOption);
        
    }

    public void delete(CustomUserDetails userDetails, Long sheltersId, Long dogsId, Long optionsId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(() -> new NotFoundException(errorMessages.DOG_NOT_FOUND));

        if (!dog.getShelter().getId().equals(shelter.getId())){
            throw new ForbiddenException(errorMessages.NOT_YOUR_DOG);
        }

        WalkOption walkOption = walkOptionRepository.findById(optionsId)
                .orElseThrow(()-> new NotFoundException("해당 옵션 정보가 존재하지 않습니다."));

        walkOptionRepository.delete(walkOption);
    }

}
