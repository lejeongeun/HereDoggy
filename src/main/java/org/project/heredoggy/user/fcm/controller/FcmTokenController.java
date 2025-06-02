package org.project.heredoggy.user.fcm.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.fcm.dto.FcmTokenRequestDTO;
import org.project.heredoggy.user.fcm.service.FcmTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> saveToken(@RequestBody FcmTokenRequestDTO request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        fcmTokenService.saveOrUpdate(request.getToken(), AuthUtils.getValidMember(userDetails));
        return ResponseEntity.ok(Map.of("message", "fcm 토큰 등록 완료"));
    }

    @DeleteMapping("/token")
    public ResponseEntity<Map<String, String>> deleteToken(@AuthenticationPrincipal CustomUserDetails userDetails) {
        fcmTokenService.deleteByMember(AuthUtils.getValidMember(userDetails));
        return ResponseEntity.ok(Map.of("message", "fcm 토큰 삭제 완료"));
    }
}