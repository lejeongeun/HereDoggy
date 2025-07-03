package org.project.heredoggy.shelter.volunteer.unavailableDate.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.volunteer.VolunteerUnavailableRepository;
import org.project.heredoggy.domain.postgresql.volunteer.VolunteerUnavailableTime;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.volunteer.unavailableDate.dto.VolunteerUnavailableRequestDTO;
import org.project.heredoggy.shelter.volunteer.unavailableDate.dto.VolunteerUnavailableResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VolunteerUnavailableService {
    private final VolunteerUnavailableRepository volunteerUnavailableRepository;

    public void create(CustomUserDetails userDetails, VolunteerUnavailableRequestDTO requestDTO) {
        Shelter shelter = SheltersAuthUtils.getValidMember(userDetails).getShelter();

        VolunteerUnavailableTime res = VolunteerUnavailableTime.builder()
                .shelter(shelter)
                .date(requestDTO.getDate())
                .startTime(requestDTO.getStartTime())
                .endTime(requestDTO.getEndTime())
                .reason(requestDTO.getReason())
                .build();

        volunteerUnavailableRepository.save(res);
    }

    public List<VolunteerUnavailableResponseDTO> getAllForCurrentShelter(CustomUserDetails userDetails) {
        Shelter shelter = SheltersAuthUtils.getValidMember(userDetails).getShelter();

        return volunteerUnavailableRepository.findByShelter(shelter).stream()
                .map(unavailable -> VolunteerUnavailableResponseDTO.builder()
                        .id(unavailable.getId())
                        .date(unavailable.getDate())
                        .startTime(unavailable.getStartTime())
                        .endTime(unavailable.getEndTime())
                        .reason(unavailable.getReason())
                        .build()
                ).collect(Collectors.toList());
    }

    public void delete(CustomUserDetails userDetails, Long unavailableId) {
        Shelter shelter = SheltersAuthUtils.getValidMember(userDetails).getShelter();

        VolunteerUnavailableTime target = volunteerUnavailableRepository.findById(unavailableId)
                .orElseThrow(() -> new NotFoundException("해당 시간 정보를 찾을 수 없습니다."));

        if (!target.getShelter().getId().equals(shelter.getId())) {
            throw new ForbiddenException("삭제 권한이 없습니다.");
        }

        volunteerUnavailableRepository.deleteById(unavailableId);
    }
}
