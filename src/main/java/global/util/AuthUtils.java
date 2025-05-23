package global.util;

import global.exception.InactiveAccountException;
import global.exception.UnauthorizedException;
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
}