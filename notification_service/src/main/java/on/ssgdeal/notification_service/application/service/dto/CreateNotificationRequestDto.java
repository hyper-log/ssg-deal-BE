package on.ssgdeal.notification_service.application.service.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateNotificationRequestDto(
        String senderSlackEmail,
        String receiverSlackEmail,
        Long orderId,
        String ordererName,
        LocalDateTime orderAt,
        Long paymentPrice,
        String orderStatus
) {
}
