package on.ssgdeal.order_service.infrastructure.client.slack.feign;

import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slack-service")
public interface SlackServiceFeignClient {

    @PostMapping("/internal/v1/slack/order/complete")
    CommonResponse<OrderCompleteSendSlackResponseDto> sendOrderCompleteMessage(
        @RequestBody OrderCompleteSendSlackRequestDto requestDto);
}
