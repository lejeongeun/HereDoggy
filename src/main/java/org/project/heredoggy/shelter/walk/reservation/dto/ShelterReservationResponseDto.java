package org.project.heredoggy.shelter.walk.reservation.dto;

import lombok.*;
import org.project.heredoggy.domain.postgresql.dog.DogStatus;
import org.project.heredoggy.domain.postgresql.walk.reservation.WalkReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShelterReservationResponseDto {
    private Long id;

    // 사용자 이름, 이메일, 연락처
    private String memberName;
    private String memberEmail;
    private String memberPhone;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String note; // 메모

    private Long dogId;
    private String dogName;
    private DogStatus dogStatus;

    private WalkReservationStatus walkReservationStatus;
    private LocalDateTime createAt;
    private LocalDateTime decisionAt; // 관리자 요청 승인 시간

}
