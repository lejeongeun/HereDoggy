package org.project.heredoggy.user.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.user.fcm.service.FcmTokenService;
import org.project.heredoggy.user.member.dto.request.*;
import org.project.heredoggy.user.member.dto.response.TokenResponseDTO;
import org.project.heredoggy.user.member.dto.response.ReissueResponseDTO;
import org.project.heredoggy.user.member.service.AuthService;
import org.project.heredoggy.user.member.service.RedisService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberAuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final FcmTokenService fcmTokenService;
    private final RedisService redisService;
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody MemberSignUpRequestDTO request) {
        authService.signUp(request);
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Member member = ((CustomUserDetails)authentication.getPrincipal()).getMember();
        String accessToken = jwtTokenProvider.generateAccessToken(member.getId());

        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());
            redisService.saveRefreshToken(member.getId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshToken));
    }
    @PostMapping("/reissue")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> reissue(@RequestBody ReissueRequestDTO request) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(request.getRefreshToken());

        String storedRefreshToken = redisService.getRefreshToken(memberId);
        if (!storedRefreshToken.equals(request.getRefreshToken())) {
            throw new IllegalArgumentException("RefreshToken이 유효하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(memberId);

        return ResponseEntity.ok(new ReissueResponseDTO(newAccessToken));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestBody String token) {
        redisService.deleteRefreshToken(userDetails.getMember().getId());
        fcmTokenService.deleteToken(token, userDetails.getMember());
        return ResponseEntity.ok("로그아웃 완료");
    }

    //이메일 찾기
    @PostMapping("/find-email")
    public ResponseEntity<Map<String, String>> findEmail(@RequestBody FindEmailRequestDTO request) {
        authService.findEmailByNameAndPhone(request);
        return ResponseEntity.ok(Map.of("message", "입력된 정보로 등록된 이메일이 있는 경우, 해당 이메일로 아이디 정보를 전송했습니다."));
    }

    // 비밀번호 재설정 - gmail로 토큰 받기
    @PostMapping("/password-reset-reqeust")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody PasswordResetRequestDTO request) {
        authService.sendPasswordResetEmail(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 토큰이 이메일로 전송되었습니다."));
    }

    // 새로운 비밀번호 입력하여 재설정하기
    @PostMapping("/password-reset")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody PasswordResetConfirmDTO reset) {
        authService.resetPassword(reset);
        return ResponseEntity.ok(Map.of("message","비밀번호가 성공적으로 변경되었습니다."));
    }

    @PostMapping("/email-verification-request")
    public ResponseEntity<Map<String, String>> requestEmailVerification(@RequestBody EmailVerificationRequestDTO dto) {
        authService.sendEmailVerificationCode(dto.getEmail());
        return ResponseEntity.ok(Map.of("message","이메일로 인증 코드가 전송되었습니다."));
    }

    @PostMapping("/email-verification-confirm")
    public ResponseEntity<Map<String, String>> confirmEmailVerification(@RequestBody EmailVerificationConfirmDTO dto) {
        authService.verifyEmailCode(dto.getEmail(), dto.getCode());
        return ResponseEntity.ok(Map.of("message","이메일 인증이 완료되었습니다."));
    }
}
