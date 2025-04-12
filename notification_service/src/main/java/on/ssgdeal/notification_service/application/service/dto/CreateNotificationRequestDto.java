package on.ssgdeal.notification_service.application.service.dto;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.entity.dto.CreateNotificationDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record CreateNotificationRequestDto(
        String senderSlackEmail,
        String receiverSlackEmail,
        Long totalOrderId,
        String ordererName,
        LocalDate orderAt,
        Long paymentPrice,
        String orderStatus
) {
    public static CreateNotificationDto toDto(
            CreateNotificationRequestDto requestDto,
            String content,
            NotificationTemplate template,
            LocalDateTime sendAt
    ) {
        return CreateNotificationDto.builder()
                .requestDto(requestDto)
                .content(content)
                .sendAt(sendAt)
                .template(template)
                .build();

    }
}
