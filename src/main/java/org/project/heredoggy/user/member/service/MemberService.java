package org.project.heredoggy.user.member.service;

import global.exception.InactiveAccountException;
import global.exception.UnauthorizedException;
import global.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.member.dto.response.MemberDetailResponseDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    public MemberDetailResponseDTO getMemberDetails(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        return MemberDetailResponseDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .phone(member.getPhone())
                .address(member.getAddress())
                .profileImageUrl(member.getProfileImageUrl())
                .role(member.getRole().name())
                .totalWalkDistance(member.getTotalWalkDistance())
                .totalWalkDuration(member.getTotalWalkDuration())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
