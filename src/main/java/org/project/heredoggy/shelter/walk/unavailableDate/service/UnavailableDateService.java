package org.project.heredoggy.shelter.walk.unavailableDate.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.domain.postgresql.walk.reservation.UnavailableDate;
import org.project.heredoggy.domain.postgresql.walk.reservation.UnavailableDateRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.unavailableDate.dto.UnavailableDateRequestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnavailableDateService {
    private final ShelterRepository shelterRepository;
    private final DogRepository dogRepository;
    private final UnavailableDateRepository unavailableDateRepository;

    public void registerUnavailableDate(CustomUserDetails userDetails, Long sheltersId, Long dogsId, UnavailableDateRequestDTO request) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        if (!shelterRepository.findById(shelter.getId()).isPresent()){
            throw new NotFoundException(ErrorMessages.SHELTERS_NOT_FOUND);
        }
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        // 중복 방지
        for (LocalDate date : request.getDates()){
            if (!unavailableDateRepository.existsByDogIdAndDate(dogsId, date)){
                UnavailableDate unavailableDate = UnavailableDate.builder()
                        .shelter(shelter)
                        .dog(dog)
                        .date(date)
                        .build();
                unavailableDateRepository.save(unavailableDate);
            }
        }
    }

    public List<LocalDate> getAllUnavailableDate(CustomUserDetails userDetails, Long sheltersId, Long dogsId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        return unavailableDateRepository.findByDogId(dog.getId()).stream()
                .map(UnavailableDate::getDate)
                .collect(Collectors.toList());
    }

    public void removeUnavailableDate(CustomUserDetails userDetails, Long sheltersId, Long dogsId, Long unavailableId) {
        Shelter shelter = SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
        if (!shelterRepository.findById(shelter.getId()).isPresent()){
            throw new BadRequestException(ErrorMessages.SHELTERS_NOT_FOUND);
        }
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        if (!dog.getShelter().getId().equals(shelter.getId())){
            throw new NotFoundException(ErrorMessages.NOT_YOUR_DOG);
        }
        UnavailableDate unavailableDate = unavailableDateRepository.findById(unavailableId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.OPTIONS_INFO_NOT_FOUND));
        unavailableDateRepository.delete(unavailableDate);
    }
}
