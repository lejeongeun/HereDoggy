package org.project.heredoggy.user.member.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.security.JwtTokenProvider;
import org.project.heredoggy.user.member.dto.response.TokenResponseDTO;
import org.project.heredoggy.user.member.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/oauth")
@RequiredArgsConstructor
public class MemberGoogleOAuthController {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        
        if (code == null) {
            return ResponseEntity.badRequest().body("Authorization code is required");
        }

        try {
            // 1. 인가 코드로 액세스 토큰 받기
            String accessToken = getGoogleAccessToken(code);
            
            // 2. 액세스 토큰으로 사용자 정보 받기
            Map<String, Object> userInfo = getGoogleUserInfo(accessToken);
            
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");
            String picture = (String) userInfo.get("picture");

            String nickname = name != null ? name : "User_" + UUID.randomUUID().toString().substring(0, 8);

            // 3. 사용자 정보로 회원가입 또는 로그인 처리
            Member member = memberRepository.findByEmail(email)
                    .orElseGet(() -> memberRepository.save(Member.builder()
                            .email(email)
                            .name(name)
                            .nickname(nickname)
                            .address("testAddress")
                            .phone("010-1234-5678")
                            .password("SOCIAL_LOGIN")
                            .role(RoleType.USER)
                            .profileImageUrl(picture != null ? picture : "DEFAULT_IMAGE")
                            .isActive(true)
                            .build()));

            // 4. JWT 토큰 발급
            String jwtAccessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());
            redisService.saveRefreshToken(member.getEmail(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

            return ResponseEntity.ok(new TokenResponseDTO(jwtAccessToken, refreshToken));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Google login failed: " + e.getMessage());
        }
    }

    private String getGoogleAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        String body = String.format(
            "grant_type=authorization_code&client_id=%s&client_secret=%s&redirect_uri=%s&code=%s",
            googleClientId,
            googleClientSecret,
            "heredoggy://callback",
            code
        );
        
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        Map<String, Object> tokenResponse = response.getBody();
        
        return (String) tokenResponse.get("access_token");
    }

    private Map<String, Object> getGoogleUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            userInfoUrl,
            HttpMethod.GET,
            request,
            Map.class
        );
        
        return response.getBody();
    }

    // 기존 방식 (호환성을 위해 유지, 나중에 삭제 예정)
    @Deprecated
    @PostMapping("/google/token")
    public ResponseEntity<?> googleLoginWithToken(@RequestBody Map<String, String> body) {
        String idTokenString = body.get("token");

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String picture = (String) payload.get("picture");

                String nickname = name != null ? name : "User_" + UUID.randomUUID().toString().substring(0, 8);

                Member member = memberRepository.findByEmail(email)
                        .orElseGet(() -> memberRepository.save(Member.builder()
                                .email(email)
                                .name(name)
                                .nickname(nickname)
                                .address("testAddress")
                                .phone("010-1234-5678")
                                .password("SOCIAL_LOGIN")
                                .role(RoleType.USER)
                                .profileImageUrl(picture != null ? picture : "DEFAULT_IMAGE")
                                .isActive(true)
                                .build()));

                String accessToken = jwtTokenProvider.generateAccessToken(member.getEmail());
                String refreshToken = jwtTokenProvider.generateRefreshToken(member.getEmail());
                redisService.saveRefreshToken(member.getEmail(), refreshToken, jwtTokenProvider.getRefreshTokenExpiration());

                return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshToken));
            } else {
                return ResponseEntity.badRequest().body("Invalid ID token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Token verification failed: " + e.getMessage());
        }
    }
}
