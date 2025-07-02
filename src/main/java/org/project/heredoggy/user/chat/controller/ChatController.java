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

        // 시스템 문의일 경우 Gemini 호출 생략
        if (chatService.isSystemInquiry(request.getMessage())) {
            String systemReply = "해당 내용은 여기보개 운영팀이 직접 확인해야 하는 사항이에요 🙏\n" +
                    "문의하기 메뉴를 이용해서 남겨주시면 빠르게 도와드릴게요!";
            chatService.saveMessage(memberId, "user", request.getMessage());
            chatService.saveMessage(memberId, "model", systemReply);
            return ResponseEntity.ok(Map.of("reply", systemReply));
        }

        // 일반 질문 흐름
        List<GeminiMessage> messages = chatService.buildMessages(memberId, request.getMessage());
        String reply = geminiClient.sendMessages(messages);

        chatService.saveMessage(memberId, "user", request.getMessage());
        chatService.saveMessage(memberId, "model", reply);

        return ResponseEntity.ok(Map.of("reply", reply));
    }

    //남은 갯수 반환 ex:6/20
    @GetMapping("/limit")
    public ResponseEntity<Integer> getChatRemaining(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = AuthUtils.getValidMember(userDetails).getId();
        int remaining = chatRateLimiter.getRemaining(memberId);
        return ResponseEntity.ok(remaining);
    }
}
