package org.project.heredoggy.user.walk.reservation.service;

import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.dog.DogRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.domain.postgresql.walk.reservation.*;
import org.project.heredoggy.domain.postgresql.walk.walkRoute.WalkRoute;
import org.project.heredoggy.domain.postgresql.walk.walkRoute.WalkRouteRepository;
import org.project.heredoggy.global.error.ErrorMessages;
import org.project.heredoggy.global.exception.BadRequestException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.exception.UnauthorizedException;
import org.project.heredoggy.global.notification.ShelterSseNotificationFactory;
import org.project.heredoggy.global.util.AuthUtils;

import org.project.heredoggy.global.util.TimeUtil;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.shelter.walk.route.walkRoute.dto.WalkRouteResponseDTO;
import org.project.heredoggy.shelter.walk.route.walkRoute.mapper.WalkRouteMapper;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationRequestDTO;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationResponseDTO;
import org.project.heredoggy.user.walk.reservation.dto.UnavailableTimeResponseDTO;
import org.project.heredoggy.user.walk.reservation.mapper.MemberReservationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberReservationService {
    private final ReservationRepository reservationRepository;
    private final DogRepository dogRepository;
    private final ShelterSseNotificationFactory sseNotificationFactory;
    private final UnavailableDateRepository unavailableDateRepository;
    private final MemberReservationMapper reservationMapper;
    private final WalkRouteMapper walkRouteMapper;
    private final WalkRouteRepository walkRouteRepository;

    @Transactional(readOnly = true)
    public List<DogResponseDTO> getAllReservationDog() {
        return dogRepository.findAll().stream()
                .map(reservationMapper::toDogDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DogResponseDTO getDetailsReservationsDog(Long dogsId) {
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new BadRequestException(ErrorMessages.DOG_NOT_FOUND));
        return reservationMapper.toDogDto(dog);
    }

    @Transactional(readOnly = true)
    public List<LocalDate> getUnavailableList(CustomUserDetails userDetails, Long dogsId) {
        Member member = AuthUtils.getValidMember(userDetails);

        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));

        return unavailableDateRepository.findByDogId(dog.getId()).stream()
                .map(UnavailableDate::getDate)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UnavailableTimeResponseDTO> getReservedUnavailableTimes(Long dogsId) {
        Dog dog = dogRepository.findById(dogsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.DOG_NOT_FOUND));
        // 해당 강아지의 예약 목록 중 상태가 Pending, Approved인 것들만 List형식으로 가져오기
        List<Reservation> reservationList = reservationRepository.findByDogIdAndStatusIn(
                dog.getId(),
                List.of(WalkReservationStatus.PENDING, WalkReservationStatus.APPROVED)
        );

        // 해당 날짜에 존재하는 에약 시간대를 가져오기
        Map<LocalDate, UnavailableTimeResponseDTO> map = new HashMap<>();

        for (Reservation reservation : reservationList) {
            LocalDate date = reservation.getDate();

            // 해당 Key가 map에 없을 경우 값 삽입, 해당 날짜가 map에 존재하지 않다면 기본값을 부여
            map.putIfAbsent(date, UnavailableTimeResponseDTO.builder()
                    .date(date)
                    .morningUnavailable(false)
                    .afternoonUnavailable(false)
                    .build());

            LocalTime startTime = reservation.getStartTime();

            if (TimeUtil.isMorning(startTime)){
                map.get(date).setMorningUnavailable(true);
            }
            if (TimeUtil.isAfternoon(startTime)){
                map.get(date).setAfternoonUnavailable(true);
            }
        }
        return new ArrayList<>(map.values());
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

    @Transactional(readOnly = true)
    public List<MemberReservationResponseDTO> getAllReservation(CustomUserDetails userDetails) {
        Member member = userDetails.getMember();

        return reservationRepository.findByMember(member).stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberReservationResponseDTO getDetailsReservation(CustomUserDetails userDetails, Long reservationsId) {
        Member member = userDetails.getMember();
        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.RESERVATION_NOT_FOUND));
        if (!reservation.getMember().getId().equals(member.getId())){
            throw new BadRequestException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        return reservationMapper.toDto(reservation);
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public List<WalkRouteResponseDTO> getAllWalkRouteCheck(CustomUserDetails userDetails, Long reservationsId) {
        Member member = AuthUtils.getValidMember(userDetails);

        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));
        if (!member.getId().equals(reservation.getMember().getId())){
            throw new UnauthorizedException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }
        Long shelterId = reservation.getShelter().getId();
        // 보호소마다 기본 경로 보유 -> 보호소를 기준으로 walkRoute조회
        List<WalkRoute> walkRoutes = walkRouteRepository.findAllByShelterId(shelterId);

        return walkRoutes.stream()
                .map(walkRouteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WalkRouteResponseDTO getDetailsWalkRouteCheck(CustomUserDetails userDetails, Long reservationsId, Long walkRoutesId) {
        Member member = AuthUtils.getValidMember(userDetails);

        Reservation reservation = reservationRepository.findById(reservationsId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.RESERVATION_NOT_FOUND));

        if (!member.getId().equals(reservation.getMember().getId())){
            throw new UnauthorizedException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        WalkRoute walkRoute = walkRouteRepository.findById(walkRoutesId)
                .orElseThrow(()-> new NotFoundException(ErrorMessages.WALK_ROUTE_NOT_FOUND));
        if (!walkRoute.getShelter().getId().equals(reservation.getShelter().getId())){
            throw new UnauthorizedException("해당 보호소의 경로가 아닙니다.");
        }
        return walkRouteMapper.toDto(walkRoute);
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


}