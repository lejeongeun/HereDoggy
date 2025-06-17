package org.project.heredoggy.user.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.config.oauth.KakaoApiClient;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.security.JwtTokenProvider;
import org.project.heredoggy.user.member.dto.request.KakaoLoginRequest;
import org.project.heredoggy.user.member.dto.response.TokenResponseDTO;
import org.project.heredoggy.user.member.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/oauth")
@RequiredArgsConstructor
public class MemberKakaoOAuthController {

    private final KakaoApiClient kakaoApiClient;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenResponseDTO> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        Map<String, Object> userInfo = kakaoApiClient.getUserInfo(request.getToken());
        @SuppressWarnings("unchecked")
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        @SuppressWarnings("unchecked")
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String name = (String) kakaoAccount.get("name");
        String phone = (String) kakaoAccount.get("phone_number");
        String nickname = profile != null ? (String) profile.get("nickname") : null;

        if (nickname == null || nickname.isBlank()) {
            nickname = "User_" + UUID.randomUUID().toString().substring(0, 8);
        }

        final String finalNickname = nickname;

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(email)
                        .name(name)
                        .nickname(finalNickname)
                        .password("SOCIAL_LOGIN")
                        .role(RoleType.USER)
                        .phone(phone)
                        .address("소셜 주소")
                        .profileImageUrl("DEFAULT_IMAGE")
                        .isActive(true)
                        .build()));

        String accessToken = jwtTokenProvider.generateAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());
        redisService.saveRefreshToken(member.getId(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

        return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshToken));
    }
}
