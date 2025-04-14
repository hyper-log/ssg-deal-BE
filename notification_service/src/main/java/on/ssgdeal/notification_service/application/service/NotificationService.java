package on.ssgdeal.notification_service.application.service;

import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationResponseDto;

public interface NotificationService {

    CreateNotificationResponseDto sendSlackNotification(CreateNotificationRequestDto requestDto);
}
