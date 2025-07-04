package org.project.heredoggy.user.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.config.oauth.KakaoApiClient;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.security.JwtTokenProvider;
import org.project.heredoggy.user.member.dto.response.TokenResponseDTO;
import org.project.heredoggy.user.member.service.RedisService;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        
        if (code == null) {
            return ResponseEntity.badRequest().body("Authorization code is required");
        }

        try {
            // 1. 인가 코드로 액세스 토큰 받기
            String accessToken = getKakaoAccessToken(code);
            
            // 2. 액세스 토큰으로 사용자 정보 받기
            Map<String, Object> userInfo = kakaoApiClient.getUserInfo(accessToken);
            
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

            // 3. 사용자 정보로 회원가입 또는 로그인 처리
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

            // 4. JWT 토큰 발급
            String jwtAccessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());
            redisService.saveRefreshToken(member.getEmail(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

            return ResponseEntity.ok(new TokenResponseDTO(jwtAccessToken, refreshToken));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Kakao login failed: " + e.getMessage());
        }
    }

    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        String body = String.format(
            "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
            "f672fc97c767b2220faed59a97064398", // 실제 카카오 REST API 키
            "heredoggy://callback",
            code
        );
        
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        Map<String, Object> tokenResponse = response.getBody();
        
        return (String) tokenResponse.get("access_token");
    }
}
