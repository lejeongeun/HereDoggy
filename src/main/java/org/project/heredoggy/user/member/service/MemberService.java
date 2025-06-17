package org.project.heredoggy.user.member.service;

import org.project.heredoggy.domain.postgresql.post.free.FreePostRepository;
import org.project.heredoggy.domain.postgresql.post.missing.MissingPostRepository;
import org.project.heredoggy.domain.postgresql.post.review.ReviewPostRepository;
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
import org.project.heredoggy.user.member.dto.response.MyPostResponseDTO;
import org.project.heredoggy.user.member.dto.response.PostDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final FreePostRepository freePostRepository;
    private final MissingPostRepository missingPostRepository;
    private final ReviewPostRepository reviewPostRepository;

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
        String email = AuthUtils.getValidMember(userDetails).getEmail();

        redisService.deleteRefreshToken(email);
        memberRepository.deleteByEmail(email);
    }

    public void updateNotificationSetting(CustomUserDetails userDetails, boolean isEnabled) {
        Member member = AuthUtils.getValidMember(userDetails);
        member.setIsNotificationEnabled(isEnabled);
    }

    public MyPostResponseDTO getMyFreePostList(CustomUserDetails userDetails) {
        Member member = AuthUtils.getValidMember(userDetails);

        List<PostDTO> freePosts = freePostRepository.findByWriterOrderByCreatedAtDesc(member).stream()
                .map(PostDTO::fromFreePost)
                .toList();

        List<PostDTO> reviewPosts = reviewPostRepository.findByWriterOrderByCreatedAtDesc(member).stream()
                .map(PostDTO::fromReviewPost)
                .toList();

        List<PostDTO> missingPosts = missingPostRepository.findByWriterOrderByCreatedAtDesc(member).stream()
                .map(PostDTO::fromMissingPost)
                .toList();

        return MyPostResponseDTO.builder()
                .freePosts(freePosts)
                .reviewPosts(reviewPosts)
                .missingPosts(missingPosts)
                .build();
    }
}

