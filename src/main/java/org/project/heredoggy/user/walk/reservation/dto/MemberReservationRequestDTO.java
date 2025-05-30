package org.project.heredoggy.user.walk.reservation.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberReservationRequestDTO {
    private String note; // 메모


}
