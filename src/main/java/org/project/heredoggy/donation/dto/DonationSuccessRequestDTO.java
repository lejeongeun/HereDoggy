package org.project.heredoggy.donation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationSuccessRequestDTO {
    // 결제 성공 후 toss 에서 전달되는 정보
    private String paymentKey;
    private String orderId;
    private Long amount;
}
