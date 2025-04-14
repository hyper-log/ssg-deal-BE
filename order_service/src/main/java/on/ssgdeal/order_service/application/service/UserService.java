package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;

public interface UserService {

    ValidDestinationResponseDto validDestinationRequest(ValidDestinationRequestDto dto);
}
