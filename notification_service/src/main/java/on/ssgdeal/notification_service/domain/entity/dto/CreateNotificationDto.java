package on.ssgdeal.notification_service.domain.entity.dto;

import lombok.Builder;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;

import java.time.LocalDateTime;

@Builder
public record CreateNotificationDto(
        CreateNotificationRequestDto requestDto,
        String content,
        NotificationTemplate template,
        LocalDateTime sendAt
) {
}
