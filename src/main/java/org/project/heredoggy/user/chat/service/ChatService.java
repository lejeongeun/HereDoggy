package org.project.heredoggy.user.chat.service;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.domain.postgresql.chat.ChatMessage;
import org.project.heredoggy.domain.postgresql.chat.ChatMessageRepository;
import org.project.heredoggy.domain.postgresql.chat.ChatSession;
import org.project.heredoggy.domain.postgresql.chat.ChatSessionRepository;
import org.project.heredoggy.external.gemini.dto.GeminiMessage;
import org.project.heredoggy.user.chat.limit.ChatRateLimiter;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRateLimiter chatRateLimiter;

    public List<GeminiMessage> buildMessages(Long memberId, String userInput) {
        ChatSession session = chatSessionRepository.findTopByMemberIdOrderByCreatedAtDesc(memberId)
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
        GeminiMessage persona = new GeminiMessage(
                "user",
                "넌 '여기보개'의 챗봇 보리야 🐾.\n" +
                        "너는 따뜻하고 다정한 성격으로, 반려견 산책, 입양, 보호소에 대한 질문에 귀엽고 친절하게 대답해야 해.\n" +
                        "귀여운 말버릇도 가끔 섞어줘. 너무 길지 않게 핵심 위주로 말해줘.\n" +
                        "말투는 말랑말랑하고 다정하게, 유저를 응원하는 태도를 가져줘. 이해했으면 '네~! 보리가 알려줄게요!'처럼 답변해줘"
        );
        messages.add(0, persona);

        messages.add(new GeminiMessage("user", userInput));
        return messages;
    }

    public void saveMessage(Long memberId, String role, String content) {
        ChatSession session = chatSessionRepository.findTopByMemberIdOrderByCreatedAtDesc(memberId).orElseThrow();
        chatMessageRepository.save(new ChatMessage(session, role, content));
    }
}
