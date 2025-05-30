package org.project.heredoggy.shelter.shelterAdmin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.member.dto.request.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
public class ShelterAuthController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
        try {
            // 1. 인증 시도
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // 2. 사용자 정보 추출
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            RoleType userRole = userDetails.getMember().getRole();

            // 3. 무조건 세션 생성 (세션 로그인 유지)
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            // 4. 역할에 따라 프론트 분기 유도
            if (userRole.equals(RoleType.SHELTER_ADMIN)) {
                return ResponseEntity.ok(
                        Map.of(
                                "message", "보호소 관리자 로그인 성공",
                                "role", userRole.name()
                        )
                );
            } else {
                return ResponseEntity.ok(
                        Map.of(
                                "message", "권한이 없으니 보호소 신청 페이지로 이동)",
                                "role", userRole.name(),
                                "nextAction", "/shelter-request"
                        )
                );
            }

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인 실패: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 서버 세션 무효화
        }
        SecurityContextHolder.clearContext(); // 인증 정보 초기화

        return ResponseEntity.ok("로그아웃 완료");
    }
}
