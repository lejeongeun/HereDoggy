package org.project.heredoggy.user.walk.reservation.service;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogImageResponseDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogImage;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.walk.reservation.*;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.notification.ShelterSseNotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;

import org.project.heredoggy.global.util.TimeUtil;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationRequestDTO;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberReservationService {
    private final ReservationRepository reservationRepository;
    private final DogRepository dogRepository;
    private final ShelterSseNotificationFactory sseNotificationFactory;
    private final UnavailableDateRepository unavailableDateRepository;


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
    public void requestReservation(CustomUserDetails userDetails, Long dogsId, MemberReservationRequestDTO requestDTO) {
        Member member = userDetails.getMember();

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        Shelter shelter = dog.getShelter();

        LocalDate date = requestDTO.getDate();
        LocalTime startTime = requestDTO.getStartTime();

        // 예약 불가일 체크
        if (unavailableDateRepository.existsByDogIdAndDate(dog.getId(), date)){
            throw new InvalidRequestStateException("해당 날짜는 예약할 수 없습니다.");
        }

        // 시간 중복 체크
        if (reservationRepository.existsByDogAndDateAndStartTime(dog, date, startTime)){
            throw new InvalidRequestStateException("이미 해당 시간에 예약이 존재합니다.");
        }

        // 오전 오후 예약 제한 체크
        validateTimeConflict(dog, date, startTime);

        Reservation reservation = Reservation.builder()
                .date(date)
                .startTime(startTime)
                .endTime(requestDTO.getEndTime())
                .note(requestDTO.getNote())
                .member(member)
                .dog(dog)
                .shelter(shelter)
                .status(WalkReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        reservationRepository.save(reservation);

        sseNotificationFactory.notifyWalkReservation(
                shelter.getShelterAdmin(),
                dog.getName(),
                member.getNickname(),
                reservation.getId()
        );

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

        sseNotificationFactory.notifyWalkReservationCanceled(
                reservation.getShelter().getShelterAdmin(),
                reservation.getDog().getName(),
                member.getName(),
                reservation.getId()
        );
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
                .images(dog.getImages().stream()
                        .map(img -> DogImageResponseDTO.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private void validateTimeConflict(Dog dog, LocalDate date, LocalTime newStartTime){
        List<Reservation> existingReservations = reservationRepository.findByDogIdAndDateAndStatusIn(
                dog.getId(),
                date,
                List.of(WalkReservationStatus.PENDING, WalkReservationStatus.APPROVED)
        );

        boolean hasMorning = false;
        boolean hasAfternoon = false;

        for (Reservation reservation : existingReservations){
            LocalTime start = reservation.getStartTime();
            if (TimeUtil.isMorning(start)) hasMorning = true;
            if (TimeUtil.isAfternoon(start)) hasAfternoon = true;
        }

        if (TimeUtil.isMorning(newStartTime) && hasMorning){
            throw new BadRequestException(" 해당 강아지는 오전 예약이 이미 존재합니다.");
        }

        // 오전/오후가 아닌 시간일 경우 예외 처리 (예: 12:30 ~ 13:30 같은 시간)
        if (TimeUtil.isAfternoon(newStartTime) && hasAfternoon){
            throw new BadRequestException("해당 강아지는 오후 예약이 이미 존재합니다.");
        }
    }

    public List<LocalDate> getUnavailableList(CustomUserDetails userDetails, Long dogsId) {
        Member member = AuthUtils.getValidMember(userDetails);

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        return unavailableDateRepository.findByDogId(dog.getId()).stream()
                .map(UnavailableDate::getDate)
                .collect(Collectors.toList());
    }
}
