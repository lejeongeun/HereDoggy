package org.project.heredoggy.user.notification.dto;


import lombok.Builder;
import lombok.Getter;
import org.project.heredoggy.domain.postgresql.notification.NotificationType;
import org.project.heredoggy.domain.postgresql.notification.ReferenceType;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String content;
    private NotificationType type;
    private ReferenceType referenceType;
    private Long referenceId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}