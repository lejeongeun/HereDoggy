package org.project.heredoggy.shelter.walk.reservation.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;
import org.project.heredoggy.domain.postgresql.walk.reservation.ReservationRepository;
import org.project.heredoggy.domain.postgresql.walk.reservation.WalkReservationStatus;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.NotificationFactory;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.reservation.dto.ShelterReservationResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelterReservationService {
    private final ReservationRepository reservationRepository;
    private final NotificationFactory notificationFactory;

    public List<ShelterReservationResponseDto> getAllReservations(CustomUserDetails userDetails, Long sheltersId) {
        validateShelters(userDetails, sheltersId);
        List<Reservation> reservationList = reservationRepository.findByShelterId(sheltersId);

        return reservationList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ShelterReservationResponseDto getDetailsReservations(CustomUserDetails userDetails, Long sheltersId, Long reservationsId) {
        validateShelters(userDetails, sheltersId);
        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));

        return toDto(reservation);
    }

    public void approveReservations(CustomUserDetails userDetails, Long sheltersId, Long reservationsId) {
        validateShelters(userDetails, sheltersId);
        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));

        reservation.setStatus(WalkReservationStatus.APPROVED);
        reservation.setDecisionAt(LocalDateTime.now()); // 관리자 승인 시간

        reservationRepository.save(reservation);

        notificationFactory.notifyWalkResult(
                reservation.getMember(),
                true,
                reservation.getId()
        );
    }

    public void rejectReservations(CustomUserDetails userDetails, Long sheltersId, Long reservationsId) {
        validateShelters(userDetails, sheltersId);
        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));

        reservation.setStatus(WalkReservationStatus.REJECTED);
        reservation.setDecisionAt(LocalDateTime.now());

        reservationRepository.save(reservation);

        notificationFactory.notifyWalkResult(
                reservation.getMember(),
                false,
                reservation.getId()
        );
    }

    public void cancelReservations(CustomUserDetails userDetails, Long sheltersId, Long reservationsId) {
        validateShelters(userDetails, sheltersId);
        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));

        reservation.setStatus(WalkReservationStatus.CANCELED);
        reservation.setDecisionAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    public void validateShelters(CustomUserDetails userDetails, Long sheltersId){
        SheltersAuthUtils.validateShelterAccess(userDetails, sheltersId);
    }

    public ShelterReservationResponseDto toDto(Reservation reservation){
        return ShelterReservationResponseDto.builder()
                .id(reservation.getId())
                .memberName(reservation.getMember().getName())
                .memberEmail(reservation.getMember().getEmail())
                .memberPhone(reservation.getMember().getPhone())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .note(reservation.getNote())
                .dogId(reservation.getId())
                .dogName(reservation.getDog().getName())
                .dogStatus(reservation.getDog().getStatus())
                .walkReservationStatus(reservation.getStatus())
                .createAt(reservation.getCreatedAt())
                .decisionAt(reservation.getDecisionAt())
                .build();
    }
}
