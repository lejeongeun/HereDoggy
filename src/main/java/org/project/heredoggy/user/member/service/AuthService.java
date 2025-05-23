package org.project.heredoggy.user.member.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.user.member.dto.request.MemberSignUpRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public void signUp(MemberSignUpRequestDTO request) {
        if(memberRepository.existsByEmail(request.getEmail())) {
             throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if(memberRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
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
}
