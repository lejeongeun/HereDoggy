package org.project.heredoggy.user.member.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.user.member.dto.request.MemberSignUpRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final EmailService emailService;

    public void signUp(MemberSignUpRequestDTO request) {
        if(memberRepository.existsByEmail(request.getEmail())) {
             throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if(memberRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String fullAddress = String.format("(%s) %s %s",
                request.getZipcode(),
                request.getAddress1(),
                request.getAddress2()
        );

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .birth(LocalDate.parse(request.getBirth()))
                .address(fullAddress)
                .isNotificationEnabled(true)
                .isActive(true)
                .role(RoleType.USER)
                .totalWalkDistance(0.0)
                .totalWalkDuration(0L)
                .profileImageUrl("default_Image")
                .build();

        memberRepository.save(member);
    }

    public void sendPasswordResetEmail(String email) {
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일의 회원이 존재하지 않습니다."));

        String token = UUID.randomUUID().toString();
        redisService.savePasswordResetToken(email, token); // 30분 유효

        emailService.sendPasswordResetCode(email, token); // ⭐ 핵심!
    }

    public void resetPassword(String token, String newPassword) {
        String email = redisService.getEmailByResetToken(token);
        if (email == null) throw new NotFoundException("유효하지 않거나 만료된 토큰입니다.");

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        redisService.deletePasswordResetToken(token);
    }
}