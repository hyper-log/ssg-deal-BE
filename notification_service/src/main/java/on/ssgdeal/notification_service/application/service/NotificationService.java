package on.ssgdeal.notification_service.application.service;

import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.presentation.internal.dto.CreateNotificationResponse;

public interface NotificationService {

    CreateNotificationResponse sendSlackNotification(CreateNotificationRequestDto requestDto);
}
