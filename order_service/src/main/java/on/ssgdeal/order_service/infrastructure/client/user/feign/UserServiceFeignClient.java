package on.ssgdeal.order_service.infrastructure.client.user.feign;

import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dto.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dto.ValidDestinationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceFeignClient {

    @PostMapping("/internal/v1/users/validations/deliveries")
    CommonResponse<ValidDestinationResponseDto> validDestinationRequest(
        @RequestBody ValidDestinationRequestDto requestDto);

}
