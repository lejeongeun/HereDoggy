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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
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
