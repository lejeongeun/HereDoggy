package org.project.heredoggy.user.volunteer.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.shelter.shelter.ShelterRepository;
import org.project.heredoggy.domain.postgresql.volunteer.*;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.global.util.SheltersAuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.volunteer.dto.VolunteerReservationRequestDTO;
import org.project.heredoggy.user.volunteer.dto.VolunteerReservationResponseDTO;
import org.project.heredoggy.user.volunteer.dto.VolunteerUnavailableTimeDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerReservationService {
    private final VolunteerReservationRepository volunteerReservationRepository;
    private final VolunteerUnavailableRepository volunteerUnavailableRepository;
    private final ShelterRepository shelterRepository;

    public void reserve(CustomUserDetails userDetails, VolunteerReservationRequestDTO requestDTO) {
        Member member = AuthUtils.getValidMember(userDetails);
        Shelter shelter = shelterRepository.findById(requestDTO.getShelterId())
                .orElseThrow(() -> new NotFoundException("보호소를 찾을 수 없습니다."));

        List<VolunteerUnavailableTime> unavailableList = volunteerUnavailableRepository.findByShelterAndDate(shelter, requestDTO.getDate());

        boolean alreadyReserved = volunteerReservationRepository.existsByShelterAndDateAndStartTimeAndEndTime(
                shelter, requestDTO.getDate(), requestDTO.getStartTime(), requestDTO.getEndTime()
        );

        if(alreadyReserved) {
            throw new IllegalArgumentException("이미 해당 시간대에 다른 예약이 존재합니다.");
        }

        //(신청 끝시간 < 불가 시작시간 && 신청 시작시간 > 불가 끝시간)
        boolean isConflict = unavailableList.stream().anyMatch(unavailable ->
                requestDTO.getStartTime().isBefore(unavailable.getEndTime()) &&
                requestDTO.getEndTime().isAfter(unavailable.getStartTime())
        );

        if(isConflict) {
            throw new IllegalArgumentException("해당 시간대는 봉사 신청이 불가능합니다.");
        }

        VolunteerReservation reservation = VolunteerReservation.builder()
                .member(member)
                .shelter(shelter)
                .date(requestDTO.getDate())
                .startTime(requestDTO.getStartTime())
                .endTime(requestDTO.getEndTime())
                .isGroup(requestDTO.getIsGroup())
                .status(VReservationStatus.PENDING)
                .build();

        volunteerReservationRepository.save(reservation);
    }

    public List<VolunteerReservationResponseDTO> getMyReservations(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);
        return volunteerReservationRepository.findByMember(member).stream()
                .map(VolunteerReservationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<VolunteerUnavailableTimeDTO> getUnavailableTimes(Long shelterId, LocalDate date) {
        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new NotFoundException("보호소가 존재하지 않습니다."));

        List<VolunteerUnavailableTime> list = volunteerUnavailableRepository.findByShelterAndDate(shelter, date);
        return list.stream()
                .map(n -> new VolunteerUnavailableTimeDTO(n.getStartTime(), n.getEndTime()))
                .toList();
    }

    public void cancelPendingReservation(CustomUserDetails userDetails, Long reservationId) {
        Member member = AuthUtils.getValidMember(userDetails);
        VolunteerReservation reservation = volunteerReservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));

        if (!reservation.getMember().getId().equals(member.getId())) {
            throw new ForbiddenException("본인의 예약만 취소할 수 있습니다.");
        }

        if (reservation.getStatus() != VReservationStatus.PENDING) {
            throw new IllegalStateException("대기 상태 예약만 즉시 취소할 수 있습니다.");
        }

        reservation.setStatus(VReservationStatus.CANCELED);
        volunteerReservationRepository.save(reservation);
    }

    public void requestCancel(CustomUserDetails userDetails, Long id) {
        Member member = AuthUtils.getValidMember(userDetails);
        VolunteerReservation reservation = volunteerReservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));

        if (!reservation.getMember().getId().equals(member.getId())) {
            throw new ForbiddenException("본인의 예약만 취소 요청할 수 있습니다.");
        }

        if (reservation.getStatus() != VReservationStatus.ACCEPTED) {
            throw new IllegalStateException("승인된 예약만 취소 요청할 수 있습니다.");
        }

        reservation.setStatus(VReservationStatus.CANCEL_REQUEST);
        volunteerReservationRepository.save(reservation);
    }
}