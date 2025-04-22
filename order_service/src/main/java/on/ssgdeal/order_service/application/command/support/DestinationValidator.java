package on.ssgdeal.order_service.application.command.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.UserService;
import on.ssgdeal.order_service.exception.OrderException.OrderValidDestination;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DestinationValidator {

    private final UserService userService;

    public ValidDestinationResponseDto valid(Long destinationId) {
        ValidDestinationRequestDto validDestinationRequest = ValidDestinationRequestDto.from(
            destinationId);
        try {
            log.info("배송지 검증 요청: {}", validDestinationRequest.destinationId());
            var validDestinationResponseDto = userService.validDestinationRequest(
                validDestinationRequest);
            log.info("배송지 검증 완료");
            return validDestinationResponseDto;
        } catch (Exception e) {
            log.info("배송지 검증 실패: {}", e.getMessage());
            throw new OrderValidDestination();
        }
    }
}
