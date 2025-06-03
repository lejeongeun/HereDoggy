package org.project.heredoggy.user.walk.reservation.service;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;
import org.project.heredoggy.domain.postgresql.walk.reservation.ReservationRepository;
import org.project.heredoggy.domain.postgresql.walk.reservation.WalkReservationStatus;
import org.project.heredoggy.domain.postgresql.walk.walkOption.WalkOption;
import org.project.heredoggy.domain.postgresql.walk.walkOption.WalkOptionRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.TimeUtil;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationRequestDTO;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberReservationService {
    private final ReservationRepository reservationRepository;
    private final WalkOptionRepository walkOptionRepository;
    private final DogRepository dogRepository;

    public List<DogResponseDTO> getAllReservationDog() {
        return dogRepository.findAll().stream()
                .map(this::toDogDto)
                .collect(Collectors.toList());
    }

    public DogResponseDTO getDetailsReservationsDog(Long dogsId) {

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new BadRequestException(ErrorMessages.DOG_NOT_FOUND));

        return toDogDto(dog);
    }

    // 예약 신청
    @Transactional
    public void requestReservation(CustomUserDetails userDetails, Long dogsId, Long walkOptionsId, MemberReservationRequestDTO requestDTO) {
        Member member = userDetails.getMember();

        WalkOption walkOption = walkOptionRepository.findById(walkOptionsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.OPTIONS_INFO_NOT_FOUND));

        if (!walkOption.getDog().getId().equals(dogsId)){
            throw new InvalidRequestStateException("해당 강아지에 대한 산책 옵션이 아닙니다.");
        }
        // 중복 예약 체크
        validateDuplicateReservation(member, walkOption);
        validateTimeConflict(walkOption);

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
                .walkOptionsId(reservation.getWalkOption().getId())
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

    public DogResponseDTO toDogDto(Dog dog){
        return DogResponseDTO.builder()
                .id(dog.getId())
                .name(dog.getName())
                .age(dog.getAge())
                .gender(dog.getGender())
                .weight(dog.getWeight())
                .isNeutered(dog.getIsNeutered())
                .status(dog.getStatus())
                .foundLocation(dog.getFoundLocation())
                .imagesUrls(dog.getImages().stream()
                        .map(DogImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }

    private void validateTimeConflict(WalkOption newOptions){
        List<Reservation> existingReservations = reservationRepository.findByDogIdAndDateAndStatusIn(
                newOptions.getDog().getId(),
                newOptions.getDate(),
                List.of(WalkReservationStatus.PENDING, WalkReservationStatus.APPROVED)
        );

        boolean hasMorning = false;
        boolean hasAfternoon = false;

        for (Reservation reservation : existingReservations){
            LocalTime start = reservation.getStartTime();
            if (TimeUtil.isMorning(start)) hasMorning = true;
            if (TimeUtil.isAfternoon(start)) hasAfternoon = true;
        }
        if (TimeUtil.isMorning(newOptions.getStartTime()) && hasMorning){
            throw new BadRequestException(" 해당 강아지는 오전 예약이 이미 존재합니다.");
        }

        if (TimeUtil.isAfternoon(newOptions.getStartTime()) && hasAfternoon){
            throw new BadRequestException("해당 강아지는 오후 예약이 이미 존재합니다.");
        }
    }
}
