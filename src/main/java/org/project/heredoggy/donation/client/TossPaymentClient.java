package org.project.heredoggy.donation.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.donation.dto.DonationSuccessRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TossPaymentClient {
    private final RestTemplate restTemplate;
    private final Environment environment;

    @Value("${toss.test.secret-key}")
    private String secretKey;
    @Value("${toss.test.success-url}")
    private String successUrl;
    @Value("${toss.test.fail-url}")
    private String failUrl;

    private final String BASE_URL = "https://api.tosspayments.com/v1";

    // 결제 요청 -> paymentUrl 반환
    public String createPaymentRequest(String orderId, Long amount, String orderName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(secretKey, "");

        Map<String, Object> body = Map.of(
                "orderId", orderId,
                "amount", amount,
                "orderName", orderName,
                "successUrl", successUrl,
                "failUrl", failUrl
        );
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try{
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    BASE_URL + "/payments",
                    request,
                    Map.class
            );
            log.info("Toss 결제 URL 생성 성공: {} ", response.getBody());
            return (String) response.getBody().get("paymentUrl");
        } catch (HttpClientErrorException e){
            log.error("Toss 결제 요청 실패: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Toss 결제 요청 실패");
        }
    }

    public void confirmPayment(DonationSuccessRequestDTO dto){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(secretKey, "");
        Map<String, Object> body = Map.of(
                "payment", dto.getPaymentKey(),
                "orderId", dto.getOrderId(),
                "amount", dto.getAmount()
        );
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try{
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    BASE_URL + "/payments/confirm",
                    request,
                    Map.class
            );
            log.info("Toss 결제 승인 성공: {}", response.getBody());
        }catch (HttpClientErrorException e){
            log.error("Toss 결제 승인 실패: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Toss 결제 승인 실패");
        }

    }
}
