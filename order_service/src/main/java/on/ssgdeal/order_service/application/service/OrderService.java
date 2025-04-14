package on.ssgdeal.order_service.application.service;

import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.order_service.application.service.dto.CancelOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.CancelOrderResponseDto;
import on.ssgdeal.order_service.application.service.dto.CancelTotalOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.CancelTotalOrderResponseDto;
import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.GetTotalOrderDetailResponseDto;
import on.ssgdeal.order_service.application.service.dto.GetTotalOrdersResponseDto;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.service.dto.UpdateCancelOrderSuccessRequestDto;
import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderFailRequestDto;
import on.ssgdeal.order_service.application.service.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;
import on.ssgdeal.order_service.presentation.internal.dto.ValidTotalOrderResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    CreateOrderResponse createOrder(CreateOrderRequestDto request, LoginUserInfoDto loginUserInfo);

    void createTotalOrderPaymentSuccess(UpdateTotalOrderSuccessRequestDto requestDto,
        LoginUserInfoDto loginUserInfo);

    PageDto<GetTotalOrdersResponseDto> getTotalOrders(LoginUserInfoDto loginUserInfo,
        Pageable pageable);

    GetTotalOrderDetailResponseDto getTotalOrderDetail(Long id, LoginUserInfoDto loginUserInfo);

    ValidTotalOrderResponse validTotalOrder(Long totalOrderId);

    CancelTotalOrderResponseDto cancelTotalOrder(CancelTotalOrderRequestDto request);

    void createTotalOrderPaymentFail(UpdateTotalOrderFailRequestDto requestDto);

    CancelOrderResponseDto cancelOrder(CancelOrderRequestDto request);

    void cancelOrderPaymentSuccess(UpdateCancelOrderSuccessRequestDto requestDto);
}
