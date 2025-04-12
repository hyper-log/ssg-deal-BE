package on.ssgdeal.order_service.infrastructure.client.slack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.SlackService;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.SlackServiceFeignClient;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dtos.OrderCompleteSendSlackResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SlackServiceImpl")
public class SlackServiceImpl implements SlackService {

    private final SlackServiceFeignClient feignClient;

    @Override
    public OrderCompleteSendSlackResponseDto sendOrderCompleteMessage(
        OrderCompleteSendSlackRequestDto requestDto) {
        log.info("sendOrderCompleteMessage: {}", requestDto);
        var orderCompleteSendSlackResponseDto = feignClient.sendOrderCompleteMessage(requestDto);
        return orderCompleteSendSlackResponseDto.data();
    }
}
