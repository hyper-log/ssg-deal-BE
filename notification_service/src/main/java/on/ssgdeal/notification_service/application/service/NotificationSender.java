package on.ssgdeal.notification_service.application.service;

import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationResponseDto;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationChannelType;

public interface NotificationSender {
    NotificationChannelType getChannelType();
    CreateNotificationResponseDto sendNotification(
            CreateNotificationRequestDto requestDto,
            String content,
            NotificationTemplate template
    );
}
