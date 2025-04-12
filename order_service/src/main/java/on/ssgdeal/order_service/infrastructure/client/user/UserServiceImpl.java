package on.ssgdeal.order_service.infrastructure.client.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.UserService;
import on.ssgdeal.order_service.infrastructure.client.user.feign.UserServiceFeignClient;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserServiceFeignClient feignClient;

    @Override
    public ValidDestinationResponseDto validDestinationRequest(ValidDestinationRequestDto dto) {
        log.info("validDestinationRequest : {}", dto.toString());
        var validDestinationResponseDto = feignClient.validDestinationRequest(dto);
        return validDestinationResponseDto.data();
    }
}