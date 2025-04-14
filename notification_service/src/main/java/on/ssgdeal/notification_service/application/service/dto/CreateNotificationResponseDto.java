package on.ssgdeal.notification_service.application.service.dto;

import jakarta.validation.constraints.NotBlank;
import on.ssgdeal.notification_service.domain.entity.Notification;

public record CreateNotificationResponseDto(
        @NotBlank Long notificationId
) {
    public static CreateNotificationResponseDto from(Notification notification) {
        return new CreateNotificationResponseDto(
                notification.getId()
        );
    }
}