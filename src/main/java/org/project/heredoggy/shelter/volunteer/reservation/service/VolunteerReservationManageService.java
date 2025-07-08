package org.project.heredoggy.shelter.volunteer.reservation.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.volunteer.VReservationStatus;
import org.project.heredoggy.domain.postgresql.volunteer.VolunteerReservation;
import org.project.heredoggy.domain.postgresql.volunteer.VolunteerReservationRepository;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.volunteer.dto.VolunteerReservationResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerReservationManageService {

    private final VolunteerReservationRepository volunteerReservationRepository;
    public List<VolunteerReservationResponseDTO> getReservationForShelter(CustomUserDetails userDetails) {
        Shelter shelter = SheltersAuthUtils.getValidMember(userDetails).getShelter();

        List<VolunteerReservation> reservations = volunteerReservationRepository.findByShelter(shelter);

        return reservations.stream()
                .map(VolunteerReservationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void updateReservationStatus(CustomUserDetails userDetails, Long reservationId, String statusStr) {
        Shelter shelter = SheltersAuthUtils.getValidMember(userDetails).getShelter();

        VolunteerReservation reservation = volunteerReservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));

        if (!reservation.getShelter().getId().equals(shelter.getId())) {
            throw new ForbiddenException("이 보호소의 예약이 아닙니다.");
        }

        VReservationStatus status;
        try {
            status = VReservationStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 상태 값입니다.");
        }

        reservation.setStatus(status);
        volunteerReservationRepository.save(reservation);
    }

    public void cancelReservationStatus(CustomUserDetails userDetails, Long reservationId) {
        Shelter shelter = SheltersAuthUtils.getValidMember(userDetails).getShelter();

        VolunteerReservation reservation = volunteerReservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));

        if(!reservation.getShelter().getId().equals(shelter.getId())) {
            throw new ForbiddenException("이 보호소의 예약이 아닙니다.");
        }

        if(reservation.getStatus() != VReservationStatus.CANCEL_REQUEST) {
            throw new IllegalArgumentException("취소 요청 상태가 아닙니다.");
        }

        reservation.setStatus(VReservationStatus.CANCELED);
        volunteerReservationRepository.save(reservation);
    }
}
