package org.project.heredoggy.user.chat.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.config.PersonaConfig;
import org.project.heredoggy.domain.postgresql.chat.ChatMessage;
import org.project.heredoggy.domain.postgresql.chat.ChatMessageRepository;
import org.project.heredoggy.domain.postgresql.chat.ChatSession;
import org.project.heredoggy.domain.postgresql.chat.ChatSessionRepository;
import org.project.heredoggy.external.gemini.dto.GeminiMessage;
import org.project.heredoggy.user.chat.limit.ChatRateLimiter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRateLimiter chatRateLimiter;
    private final PersonaConfig personaConfig;
    private final List<String> systemKeywords = List.of("오류", "버그", "신고", "탈퇴", "로그인", "운영팀", "관리자", "제안", "개선", "문제");
    public List<GeminiMessage> buildMessages(Long memberId, String userInput) {

        ChatSession session = chatSessionRepository.findByMemberIdAndCreatedDate(memberId, LocalDate.now())
                .orElseGet(() -> chatSessionRepository.save(new ChatSession(memberId)));

        List<ChatMessage> history = chatMessageRepository.findTop10BySessionIdOrderByCreatedAtDesc(session.getId());

        List<GeminiMessage> messages = history.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(m -> new GeminiMessage(m.getRole(), m.getContent()))
                .collect(Collectors.toList());

        if (chatRateLimiter.isLimitExceeded(memberId)) {
            throw new IllegalStateException("오늘의 질문 횟수를 모두 사용했어요! 내일 다시 만나요 🐾");
        }

        chatRateLimiter.increment(memberId); // 질문 1회 추가

        // 페르소나
        messages.add(0, personaConfig.getPersonaMessage());

        messages.add(new GeminiMessage("user", userInput));
        return messages;
    }

    public void saveMessage(Long memberId, String role, String content) {
        ChatSession session = chatSessionRepository.findByMemberIdAndCreatedDate(memberId, LocalDate.now())
                .orElseThrow();
        chatMessageRepository.save(new ChatMessage(session, role, content));
    }

    public boolean isSystemInquiry(String userInput) {
        return systemKeywords.stream().anyMatch(userInput::contains);
    }
}
