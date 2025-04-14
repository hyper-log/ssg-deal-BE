package on.ssgdeal.notification_service.application.service.dto;

import lombok.Builder;

@Builder
public record SendSlackNotificationRequestDto(
    String receiverSlackEmail,
    String content
) {
}
