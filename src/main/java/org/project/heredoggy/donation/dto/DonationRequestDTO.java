package org.project.heredoggy.donation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationRequestDTO {
    @NotNull(message = "후원 금액은 필수입니다.")
    private Long amount;

    private String orderName; // 후원자 별칭: ex)00단체
}
