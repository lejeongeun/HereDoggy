package org.project.heredoggy.global.util;

import org.project.heredoggy.global.exception.InactiveAccountException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.exception.UnauthorizedException;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.security.CustomUserDetails;

public class AuthUtils {

    public static Member getValidMember(CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        Member member = userDetails.getMember();
        if (!member.getIsActive()) {
            throw new InactiveAccountException("비활성화된 계정입니다.");
        }
        return member;
    }

    public static Member validateMemberAccess(CustomUserDetails userDetails, Long memberId){
        Member member = getValidMember(userDetails);

        if(!member.getId().equals(memberId)){
            throw new NotFoundException("회원정보를 찾을 수 없습니다.");
        }
        return member;
    }
}