package on.ssgdeal.order_service.application.service;

import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.GetTotalOrdersResponseDto;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    CreateOrderResponse createOrder(CreateOrderRequestDto request, LoginUserInfoDto loginUserInfo);

    void createTotalOrderPaymentSuccess(UpdateTotalOrderSuccessRequestDto requestDto,
        LoginUserInfoDto loginUserInfo);

    PageDto<GetTotalOrdersResponseDto> getTotalOrders(LoginUserInfoDto loginUserInfo,
        Pageable pageable);
}
