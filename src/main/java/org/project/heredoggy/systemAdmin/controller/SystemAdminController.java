package org.project.heredoggy.systemAdmin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.systemAdmin.dto.AdminEditRequestDTO;
import org.project.heredoggy.systemAdmin.dto.AdminProfileResponseDTO;
import org.project.heredoggy.systemAdmin.service.SystemAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class SystemAdminController {
    private final SystemAdminService systemAdminService;

    @GetMapping("/profile")
    public ResponseEntity<AdminProfileResponseDTO> getSystemAdminProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        AdminProfileResponseDTO systemAdmin = systemAdminService.getProfile(userDetails);
        return ResponseEntity.ok(systemAdmin);
    }

    @PutMapping("/edit")
    public ResponseEntity<Map<String, String>> edit(@Valid @RequestBody AdminEditRequestDTO request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        systemAdminService.edit(request,userDetails);
        return ResponseEntity.ok(Map.of("message", "회원 수정 성공"));
    }

    @DeleteMapping("/removal")
    public ResponseEntity<Map<String, String>> removal(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       HttpServletRequest request) {
        systemAdminService.remove(userDetails);
        request.getSession(false).invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "회원 탈퇴 성공"));
    }
}
