package org.project.heredoggy.user.member.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.member.dto.response.MemberDetailResponseDTO;
import org.project.heredoggy.user.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않았습니다.");
        }
        MemberDetailResponseDTO member = memberService.getMemberDetails(userDetails);
        return ResponseEntity.ok(member);
    }
}
