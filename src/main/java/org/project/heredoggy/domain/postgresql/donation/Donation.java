package org.project.heredoggy.domain.postgresql.donation;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private String orderId; // Toss 결제 요청 시 생성한 고유 ID

    @Column(name = "payment_key")
    private String paymentKey; // Toss에서 결제 성공 시 전달되는 키

    @Column(name = "order_name")
    private String orderName; // 예 "HereDoggy 후원자" (후원자 지정 이름)

    private Long amount; // 금액

    @Enumerated(EnumType.STRING)
    private DonationStatus status; // 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}