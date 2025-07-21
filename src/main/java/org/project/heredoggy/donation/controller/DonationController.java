package org.project.heredoggy.donation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.donation.dto.DonationRequestDTO;
import org.project.heredoggy.donation.dto.DonationSuccessRequestDTO;
import org.project.heredoggy.donation.service.DonationService;
import org.project.heredoggy.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donations")
public class DonationController {
    private final DonationService donationService;

    // 후원 결제 요청
    @PostMapping("/request")
    public ResponseEntity<Map<String, String>> requestDonation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @Valid @RequestBody DonationRequestDTO request){
        String paymentUrl = donationService.requestDonation(userDetails.getMember(), request);
        return ResponseEntity.ok(Map.of("tossPayment", paymentUrl));
    }

    // 성공 처리
    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> successDonation(@ModelAttribute DonationSuccessRequestDTO successRequest){
        donationService.handleSuccessDonation(successRequest);
        return ResponseEntity.ok(Map.of("message", "결제가 성공적으로 처리되었습니다."));
    }

    // 실패 처리
    @GetMapping("/fail")
    public ResponseEntity<Map<String, String>> failDonation(@RequestParam String message){
        return ResponseEntity.badRequest().body(Map.of("message", "결제가 실패했습니다." + message));
    }

}
