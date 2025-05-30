package org.project.heredoggy.user.walk.reservation.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;
import org.project.heredoggy.domain.postgresql.walk.reservation.ReservationRepository;
import org.project.heredoggy.domain.postgresql.walk.reservation.WalkReservationStatus;
import org.project.heredoggy.domain.postgresql.walk.walkOption.WalkOption;
import org.project.heredoggy.domain.postgresql.walk.walkOption.WalkOptionRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationRequestDTO;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberReservationService {
    private final ReservationRepository reservationRepository;
    private final WalkOptionRepository walkOptionRepository;

    // 예약 신청
    @Transactional
    public void requestReservation(CustomUserDetails userDetails, MemberReservationRequestDTO requestDTO) {
        Member member = userDetails.getMember();
        Long optionsId = requestDTO.getWalkOptionId();
        if (optionsId == null){
            throw new IllegalArgumentException("walkOption의 id 번호를 입력해주세요");
        }
        WalkOption walkOption = walkOptionRepository.findById(requestDTO.getWalkOptionId())
                .orElseThrow(()-> new NotFoundException(ErrorMessages.OPTIONS_INFO_NOT_FOUND));
        // 중복 예약 체크
        validateDuplicateReservation(member, walkOption);

        Reservation reservation = Reservation.builder()
                .date(walkOption.getDate())
                .startTime(walkOption.getStartTime())
                .endTime(walkOption.getEndTime())
                .note(requestDTO.getNote())
                .member(member)
                .dog(walkOption.getDog())
                .shelter(walkOption.getShelter())
                .walkOption(walkOption)
                .status(WalkReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        reservationRepository.save(reservation);

    }

    public List<MemberReservationResponseDTO> getAllReservation(CustomUserDetails userDetails) {
        Member member = userDetails.getMember();

        return reservationRepository.findByMember(member).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public MemberReservationResponseDTO getDetailsReservation(CustomUserDetails userDetails, Long reservationsId) {
        Member member = userDetails.getMember();
        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.RESERVATION_NOT_FOUND));
        if (!reservation.getMember().getId().equals(member.getId())){
            throw new BadRequestException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        return toDto(reservation);
    }

    public MemberReservationResponseDTO toDto(Reservation reservation){
        return MemberReservationResponseDTO.builder()
                .id(reservation.getId())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .note(reservation.getNote())
                .dogId(reservation.getDog().getId())
                .dogName(reservation.getDog().getName())
                .shelterId(reservation.getShelter().getId())
                .shelterName(reservation.getShelter().getName())
                .walkReservationStatus(reservation.getStatus())
                .createAt(reservation.getCreatedAt())
                .build();
    }

    public void cancelRequestReservation(CustomUserDetails userDetails, Long reservationsId) {
        Member member = userDetails.getMember();
        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));
        if (!reservation.getMember().getId().equals(member.getId())){
            throw new BadRequestException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        reservation.setStatus(WalkReservationStatus.CANCELED_REQUEST);
        reservationRepository.save(reservation);
    }

    public void validateDuplicateReservation(Member member, WalkOption walkOption){
        boolean exists = reservationRepository.existsByMemberAndWalkOptionAndStatusIn(
                member, walkOption, List.of(WalkReservationStatus.PENDING, WalkReservationStatus.APPROVED));

        if (exists){
            throw new BadRequestException("이미 해당 시간에 예약이 존재합니다.");
        }
    }
}
