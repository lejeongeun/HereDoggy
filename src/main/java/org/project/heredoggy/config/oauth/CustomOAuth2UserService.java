package org.project.heredoggy.config.oauth;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.MemberRepository;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.security.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // ex) "google", "kakao"
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());

        String email = userInfo.getEmail();
        String originalNickname = userInfo.getNickname();
        String finalNickname = (originalNickname == null || originalNickname.isBlank())
                ? "User_" + UUID.randomUUID().toString().substring(0, 8)
                : originalNickname;

        // 사용자 DB 조회
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    // 자동 회원가입
                    Member newMember = Member.builder()
                            .email(email)
                            .nickname(finalNickname)
                            .password("SOCIAL_LOGIN") // 소셜 로그인은 비밀번호 사용 안 함
                            .role(RoleType.USER)
                            .isActive(true) // 필수
                            .build();
                    return memberRepository.save(newMember);
                });

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }
}