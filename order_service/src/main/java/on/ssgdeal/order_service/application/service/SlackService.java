package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackResponseDto;

public interface SlackService {

    OrderCompleteSendSlackResponseDto sendOrderCompleteMessage(
        OrderCompleteSendSlackRequestDto requestDto);
}
