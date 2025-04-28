package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackResponseDto;
import on.ssgdeal.order_service.infrastructure.messaging.dtos.OrderSuccessNotificationEvent;

public interface SlackService {

    OrderCompleteSendSlackResponseDto sendOrderCompleteMessage(
        OrderCompleteSendSlackRequestDto requestDto);

    void publishCompletedOrderSlackMessage(
        Long totalOrderId, OrderSuccessNotificationEvent payload);
}
