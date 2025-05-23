package org.project.heredoggy.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.member.dto.*;
import org.project.heredoggy.member.service.AuthService;
import org.project.heredoggy.member.service.RedisService;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
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
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
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

        return ResponseEntity.ok(new LoginResponseDTO(accessToken, refreshToken));

    }

    @PostMapping("/reissue")
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
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        redisService.deleteRefreshToken(userDetails.getMember().getId());
        return ResponseEntity.ok("로그아웃 완료");
    }
}
