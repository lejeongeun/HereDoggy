package org.project.heredoggy.user.chat.controller;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.external.gemini.GeminiClient;
import org.project.heredoggy.external.gemini.dto.GeminiMessage;
import org.project.heredoggy.global.util.AuthUtils;
import org.project.heredoggy.security.CustomUserDetails;
import org.project.heredoggy.user.chat.dto.ChatRequestDTO;
import org.project.heredoggy.user.chat.limit.ChatRateLimiter;
import org.project.heredoggy.user.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/chat")
public class ChatController {

    private final GeminiClient geminiClient;
    private final ChatService chatService;
    private final ChatRateLimiter chatRateLimiter;

    // 질문 하기
    @PostMapping
    public ResponseEntity<?> chat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChatRequestDTO request) {
        Long memberId = AuthUtils.getValidMember(userDetails).getId();
        List<GeminiMessage> messages = chatService.buildMessages(memberId, request.getMessage());
        String reply = geminiClient.sendMessages(messages);

        chatService.saveMessage(memberId, "user", request.getMessage());
        chatService.saveMessage(memberId, "model", reply);

        return ResponseEntity.ok(Map.of("reply", reply));
    }

    //남은 갯수 반환 ex:6/20
    @GetMapping("/api/chat/limit")
    public ResponseEntity<Integer> getChatRemaining(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = AuthUtils.getValidMember(userDetails).getId();
        int remaining = chatRateLimiter.getRemaining(memberId);
        return ResponseEntity.ok(remaining);
    }
}
