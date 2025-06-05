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
//    @PostMapping("/request")
//    public ResponseEntity<Map<String, String>> requestDonation(@AuthenticationPrincipal CustomUserDetails userDetails,
//                                                               @Valid @RequestBody DonationRequestDTO donationRequest){
//        String paymentUrl = donationService.requestDonation(userDetails.getMember(), donationRequest);
//        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
//    }
//
//    // 결제 성공 처리
//    @GetMapping("/success")
//    public ResponseEntity<Map<String, String>> successDonation(@ModelAttribute DonationSuccessRequestDTO requestDTO){
//        donationService.handelSuccessDonation(requestDTO);
//        return ResponseEntity.ok(Map.of("message", "후원이 성공적으로 완료되었습니다."));
//    }
//
//    // 결제 실패 처리
//    @GetMapping("/faild")
//    public ResponseEntity<Map<String, String>> failDonation(@RequestParam String message){
//        return ResponseEntity.badRequest().body(Map.of("message", "결제가 실패되었습니다." + message));
//    }

}
