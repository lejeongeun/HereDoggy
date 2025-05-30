package org.project.heredoggy.global.util;

import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.member.RoleType;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.project.heredoggy.global.exception.ForbiddenException;
import org.project.heredoggy.global.exception.InactiveAccountException;
import org.project.heredoggy.global.exception.NotFoundException;
import org.project.heredoggy.global.exception.UnauthorizedException;
import org.project.heredoggy.security.CustomUserDetails;

public class SheltersAuthUtils {
    public static Member getValidMember(CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        Member shelter = userDetails.getMember();
        if (!shelter.getIsActive()) {
            throw new InactiveAccountException("비활성화된 계정입니다.");
        }
        if(!shelter.getRole().equals(RoleType.SHELTER_ADMIN)){
            throw new ForbiddenException("SHELTER_ADMIN 권한이 필요합니다.");
        }

        return shelter;
    }
    public static Shelter validateShelterAccess(CustomUserDetails userDetails, Long sheltersId){
        Shelter shelter = getValidMember(userDetails).getShelter();

        if (!shelter.getId().equals(sheltersId)){
            throw new NotFoundException("보호소 정보를 찾을 수 없습니다.");
        }

        return shelter;
    }
}
