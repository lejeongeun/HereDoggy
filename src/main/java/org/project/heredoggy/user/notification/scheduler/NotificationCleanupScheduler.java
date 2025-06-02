package org.project.heredoggy.user.notification.scheduler;

import lombok.RequiredArgsConstructor;
import org.project.heredoggy.user.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCleanupScheduler {

    private final NotificationService notificationService;

    // 매일 0시에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUpOldNotifications() {
        notificationService.deleteOldNotifications();
    }
}