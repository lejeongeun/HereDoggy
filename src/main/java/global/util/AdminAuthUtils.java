package global.util;

import global.exception.ForbiddenException;
import global.exception.InactiveAccountException;
import global.exception.UnauthorizedException;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.security.CustomUserDetails;

public class AdminAuthUtils {

    public static Member getValidMember(CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        Member admin = userDetails.getMember();
        if (!admin.getIsActive()) {
            throw new InactiveAccountException("비활성화된 계정입니다.");
        }
        if(!admin.getRole().equals(RoleType.SYSTEM_ADMIN)){
            throw new ForbiddenException("SYSTEM_ADMIN 권한이 필요합니다.");
        }
        return admin;
    }
}