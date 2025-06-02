package org.project.heredoggy.user.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.project.heredoggy.domain.postgresql.notification.Notification;
import org.project.heredoggy.domain.postgresql.notification.NotificationRepository;
import org.project.heredoggy.domain.postgresql.notification.NotificationType;
import org.project.heredoggy.domain.postgresql.notification.ReferenceType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSseService {

    private final NotificationRepository notificationRepository;

    // [1] 한 유저가 여러 탭에서 접속할 수 있도록 List<SseEmitter>
    private final Map<Long, List<SseEmitter>> emitterMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public SseEmitter connect(Long memberId) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 유지

        emitterMap.computeIfAbsent(memberId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(memberId, emitter));
        emitter.onTimeout(() -> removeEmitter(memberId, emitter));
        emitter.onError((e) -> removeEmitter(memberId, emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE 연결 완료"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
            } catch (IOException e) {
                emitter.complete();
                removeEmitter(memberId, emitter);
            }
        }, 0, 30, TimeUnit.SECONDS);

        return emitter;
    }

    private void removeEmitter(Long memberId, SseEmitter emitter) {
        List<SseEmitter> emitters = emitterMap.get(memberId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                emitterMap.remove(memberId);
            }
        }
    }

    @Transactional
    public void saveNotification(Member receiver, String title, String content, NotificationType type,
                                 ReferenceType referenceType, Long referenceId) {
        Notification notification = Notification.builder()
                .receiver(receiver)
                .title(title)
                .content(content)
                .type(type)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    public void sendNotification(Member receiver, String title, String content, NotificationType type,
                                 ReferenceType referenceType, Long referenceId) {

        saveNotification(receiver, title, content, type, referenceType, referenceId);

        List<SseEmitter> emitters = emitterMap.get(receiver.getId());
        if (emitters != null && !emitters.isEmpty()) {
            Map<String, Object> payload = Map.of(
                    "title", title,
                    "content", content,
                    "type", type.name(),
                    "referenceType", referenceType.name(),
                    "referenceId", referenceId
            );

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(payload));
                } catch (IOException e) {
                    log.warn("SSE 알림 전송 실패: {}", e.getMessage());
                    emitter.complete();
                }
            }
        }
    }
}
