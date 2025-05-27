package org.project.heredoggy.user.member.service;

import org.project.heredoggy.global.exception.ConflictException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.util.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.member.dto.request.MemberEditRequestDTO;
import org.project.heredoggy.user.member.dto.response.MemberDetailResponseDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    public MemberDetailResponseDTO getMemberDetails(CustomUserDetails userDetails) {
        Long memberId = AuthUtils.getValidMember(userDetails).getId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

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

    public void edit(@Valid MemberEditRequestDTO request, CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        if(memberRepository.existsByNicknameAndIdNot(request.getNickname(), member.getId())) {
            throw new ConflictException("이미 사용 중인 닉네임입니다.");
        }
        String fullAddress = String.format("(%s) %s %s",
                request.getZipcode(),
                request.getAddress1(),
                request.getAddress2()
        );
        member.setName(request.getName());
        member.setNickname(request.getNickname());
        member.setPhone(request.getPhone());
        member.setAddress(fullAddress);
        memberRepository.save(member);
    }

    public void remove(CustomUserDetails userDetails) {
        Long memberId = AuthUtils.getValidMember(userDetails).getId();

        redisService.deleteRefreshToken(memberId);
        memberRepository.deleteById(memberId);
    }
}
