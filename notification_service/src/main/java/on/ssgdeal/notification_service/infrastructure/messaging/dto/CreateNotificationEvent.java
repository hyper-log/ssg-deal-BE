package on.ssgdeal.notification_service.infrastructure.messaging.dto;

import java.time.LocalDate;
import on.ssgdeal.common.messaging.core.EventPayload;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;

public record CreateNotificationEvent(
    Long totalOrderId,
    String slackEmail,
    String ordererName,
    LocalDate orderAt,
    Long paymentPrice,
    String orderStatus
) implements EventPayload {

    public CreateNotificationRequestDto toDto() {
        return CreateNotificationRequestDto.builder()
            .senderSlackEmail("hyunj2034@naver.com")
            .receiverSlackEmail(slackEmail)
            .totalOrderId(totalOrderId)
            .ordererName(ordererName)
            .orderAt(orderAt)
            .paymentPrice(paymentPrice)
            .orderStatus(orderStatus)
            .build();
    }
}
