package org.project.heredoggy.user.notification.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.notification.dto.NotificationResponseDTO;
import org.project.heredoggy.user.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // 전체 알림 리스트 조회
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<NotificationResponseDTO> result = notificationService.getNotificationsFor(userDetails.getMember());
        return ResponseEntity.ok(result);
    }

    // 안 읽은 알림 리스트 조회
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadMyNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<NotificationResponseDTO> result = notificationService.getUnreadNotificationsFor(userDetails.getMember());
        return ResponseEntity.ok(result);
    }

    // 알림 전체 읽음 처리
    @PatchMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(@AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails);
        return ResponseEntity.ok(Map.of("message", "전체 읽음 처리 완료"));
    }

    // 개별 알림 읽음 처리
    @PatchMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable("id") Long id,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAsRead(id, userDetails);
        return ResponseEntity.ok(Map.of("message", "개별 알림 읽음 처리 완료"));
    }

    // 알림 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable("id") Long id,
                                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.deleteNotification(id, userDetails);
        return ResponseEntity.ok(Map.of("message", "알림 삭제 완료"));
    }


}
































