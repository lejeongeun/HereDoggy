package org.project.heredoggy.user.walk.reservation.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberReservationRequestDTO {
    @NotNull(message = "walkOptionId는 필수입니다.")
    private Long walkOptionId;
    private String note; // 메모


}
