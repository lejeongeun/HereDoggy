package org.project.heredoggy.user.walk.reservation.mapper;

import org.project.heredoggy.dog.dto.DogImageResponseDTO;
import org.project.heredoggy.dog.dto.DogResponseDTO;
import org.project.heredoggy.domain.postgresql.dog.Dog;
import org.project.heredoggy.domain.postgresql.walk.reservation.Reservation;
import org.project.heredoggy.user.walk.reservation.dto.MemberReservationResponseDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MemberReservationMapper {

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
}
