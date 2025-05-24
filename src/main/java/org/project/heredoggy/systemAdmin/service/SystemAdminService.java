package org.project.heredoggy.systemAdmin.service;

import global.exception.ConflictException;
import global.exception.NotFoundException;
import global.util.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.dto.AdminEditRequestDTO;
import org.project.heredoggy.systemAdmin.dto.AdminProfileResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemAdminService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public AdminProfileResponseDTO getProfile(CustomUserDetails userDetails) {
        Long systemAdminId = AuthUtils.getValidMember(userDetails).getId();
        Member systemAdmin = memberRepository.findById(systemAdminId)
                .orElseThrow(() -> new NotFoundException("해당 회원을 찾을 수 없습니다."));

        return AdminProfileResponseDTO.builder()
                .id(systemAdmin.getId())
                .email(systemAdmin.getEmail())
                .name(systemAdmin.getName())
                .nickname(systemAdmin.getNickname())
                .birth(systemAdmin.getBirth())
                .phone(systemAdmin.getPhone())
                .address(systemAdmin.getAddress())
                .profileImageUrl(systemAdmin.getProfileImageUrl())
                .isActive(systemAdmin.getIsActive())
                .role(systemAdmin.getRole().name())
                .createdAt(systemAdmin.getCreatedAt())
                .build();
    }

    public void edit(@Valid AdminEditRequestDTO request, CustomUserDetails userDetails) {
        Member systemAdmin = AuthUtils.getValidMember(userDetails);

        if(memberRepository.existsByNickname(request.getNickname())) {
            throw new ConflictException("이미 사용중인 닉네임입니다");
        }

        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        String fullAddress = String.format("(%s) %s %s",
            request.getZipcode(),
            request.getAddress1(),
            request.getAddress2()
        );

        systemAdmin.setName(request.getName());
        systemAdmin.setPassword(encodedPassword);
        systemAdmin.setNickname(request.getNickname());
        systemAdmin.setPhone(request.getPhone());
        systemAdmin.setAddress(fullAddress);
        memberRepository.save(systemAdmin);
    }

    public void remove(CustomUserDetails userDetails) {
        Long systemAdminId = AuthUtils.getValidMember(userDetails).getId();

        memberRepository.deleteById(systemAdminId);
    }
}
