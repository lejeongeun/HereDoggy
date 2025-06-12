package org.project.heredoggy.donation.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.donation.DonationRepository;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.donation.client.TossPaymentClient;
import org.project.heredoggy.donation.dto.DonationRequestDTO;
import org.project.heredoggy.donation.dto.DonationSuccessRequestDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
//    private final TossPaymentClient tossPaymentClient;

//    public String requestDonation(Member member, DonationRequestDTO donationRequest) {
//
//    }
//
//    public void handelSuccessDonation(DonationSuccessRequestDTO requestDTO) {
//    }
}
