package org.project.heredoggy.user.member.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.user.member.dto.request.FindEmailRequestDTO;
import org.project.heredoggy.user.member.dto.request.MemberSignUpRequestDTO;
import org.project.heredoggy.user.member.dto.request.PasswordResetConfirmDTO;
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

    //회원가입
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

    //이메일 찾기 - 이름 + 전화번호
    public void findEmailByNameAndPhone(FindEmailRequestDTO request) {
        Member member = memberRepository.findByNameAndPhone(request.getName(), request.getPhone())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));

        String email = member.getEmail();

        emailService.sendEmailFindResult(email);
    }

    //비밀번호 재설정
    //이메일 전송
    public void sendPasswordResetEmail(String email) {
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일의 회원이 존재하지 않습니다."));

        String token = UUID.randomUUID().toString();
        redisService.savePasswordResetToken(email, token); // 30분 유효

        emailService.sendPasswordResetCode(email, token); // ⭐ 핵심!
    }

    //인증코드 + 비밀번호 입력 전송
    public void resetPassword(PasswordResetConfirmDTO request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();
        String newPasswordChk = request.getNewPasswordChk();

        String email = redisService.getEmailByResetToken(token);
        if (email == null) throw new NotFoundException("유효하지 않거나 만료된 토큰입니다.");

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        if(!newPassword.equals(newPasswordChk)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        redisService.deletePasswordResetToken(token);
    }


    //이메일 인증
    public void sendEmailVerificationCode(String email) {
        String code = String.valueOf((int)(Math.random() * 900000) + 100000); // 6자리 숫자
        redisService.saveEmailVerificationCode(email, code);

        emailService.sendEmailVerificationCode(email, code);
    }

    //이메일 인증 코드 전송하기
    public void verifyEmailCode(String email, String code) {
        String storedCode = redisService.getEmailVerificationCode(email);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new IllegalArgumentException("인증 코드가 올바르지 않거나 만료되었습니다.");
        }

        redisService.deleteEmailVerificationCode(email);
    }
}