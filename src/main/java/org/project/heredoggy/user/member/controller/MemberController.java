package org.project.heredoggy.user.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.member.dto.request.MemberEditRequestDTO;
import org.project.heredoggy.user.member.dto.response.MemberDetailResponseDTO;
import org.project.heredoggy.user.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/profile")
    public ResponseEntity<MemberDetailResponseDTO> getMyDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberDetailResponseDTO member = memberService.getMemberDetails(userDetails);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/edit")
    public ResponseEntity<Map<String, String>> edit(@Valid @RequestBody MemberEditRequestDTO request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.edit(request, userDetails);
        return ResponseEntity.ok(Map.of("message","회원 수정 성공"));
    }

    @DeleteMapping("/removal")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.remove(userDetails);
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴 성공"));
    }
}
