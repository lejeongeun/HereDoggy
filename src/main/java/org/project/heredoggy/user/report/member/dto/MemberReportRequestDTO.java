package org.project.heredoggy.user.report.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberReportRequestDTO {
    private Long reportedMemberId;
    private Long reasonId;
}
