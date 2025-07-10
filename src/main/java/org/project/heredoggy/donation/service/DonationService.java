package org.project.heredoggy.donation.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.donation.Donation;
import org.project.heredoggy.domain.postgresql.donation.DonationRepository;
import org.project.heredoggy.domain.postgresql.donation.DonationStatus;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.donation.client.TossPaymentClient;
import org.project.heredoggy.donation.dto.DonationRequestDTO;
import org.project.heredoggy.donation.dto.DonationSuccessRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationService {
    private final DonationRepository donationRepository;
    private final TossPaymentClient tossPaymentClient;

    public String requestDonation(Member member, DonationRequestDTO dto) {
        // 고유한 주문 ID 생성
        String orderId = generateOrderId();
        Donation donation = Donation.builder()
                .member(member)
                .orderId(orderId)
                .amount(dto.getAmount())
                .orderName(dto.getOrderName())
                .status(DonationStatus.PENDING)
                .build();
        donationRepository.save(donation);
        return tossPaymentClient.createPaymentRequest(orderId, dto.getAmount(), dto.getOrderName());
    }

    public void handelSuccessDonation(DonationSuccessRequestDTO requestDTO) {
        tossPaymentClient.confirmPayment(requestDTO);
        Donation donation = donationRepository.findByOrderId(requestDTO.getOrderId())
                .orElseThrow(()-> new IllegalArgumentException("해당 주문 ID의 후원이 없습니다."));
        donation.setPaymentKey(requestDTO.getPaymentKey());
        donation.setStatus(DonationStatus.SUCCESS);
    }
    public String generateOrderId() {
        return "donation-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
