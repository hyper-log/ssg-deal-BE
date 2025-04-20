package on.ssgdeal.order_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.order_service.application.service.OrderService;
import on.ssgdeal.order_service.application.dto.CancelOrderRequestDto;
import on.ssgdeal.order_service.application.dto.CancelOrderResponseDto;
import on.ssgdeal.order_service.application.dto.CancelTotalOrderRequestDto;
import on.ssgdeal.order_service.application.dto.CancelTotalOrderResponseDto;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.GetTotalOrderDetailResponseDto;
import on.ssgdeal.order_service.application.dto.GetTotalOrdersResponseDto;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderRequest;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "OrderController")
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final PassportUtil passportUtil;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateOrderResponse>> createOrder(
        @Valid @RequestBody CreateOrderRequest createOrderRequest,
        HttpServletRequest httpServletRequest
    ) {
        final CreateOrderRequestDto request = createOrderRequest.toDto();
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        LoginUserInfoDto loginUserInfo = LoginUserInfoDto.from(passport);
        CreateOrderResponse response = orderService.createOrder(request, loginUserInfo);
        return ResponseEntity.ok(CommonResponse.success(response));

    }

    @GetMapping
    public ResponseEntity<CommonResponse<PageDto<GetTotalOrdersResponseDto>>> getTotalOrders(
        @PageableDefault Pageable pageable,
        HttpServletRequest httpServletRequest
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        LoginUserInfoDto loginUserInfo = LoginUserInfoDto.from(passport);
        PageDto<GetTotalOrdersResponseDto> response = orderService.getTotalOrders(loginUserInfo,
            pageable);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/{totalOrderId}")
    public ResponseEntity<CommonResponse<GetTotalOrderDetailResponseDto>> getTotalOrderDetail(
        @PathVariable Long totalOrderId,
        HttpServletRequest httpServletRequest
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        LoginUserInfoDto loginUserInfo = LoginUserInfoDto.from(passport);
        GetTotalOrderDetailResponseDto response = orderService.getTotalOrderDetail(totalOrderId,
            loginUserInfo);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PatchMapping("/{totalOrderId}/cancel")
    public ResponseEntity<CommonResponse<CancelTotalOrderResponseDto>> cancelTotalOrder(
        @PathVariable Long totalOrderId,
        HttpServletRequest httpServletRequest
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        LoginUserInfoDto loginUserInfo = LoginUserInfoDto.from(passport);
        CancelTotalOrderRequestDto request = CancelTotalOrderRequestDto.from(totalOrderId,
            loginUserInfo);
        CancelTotalOrderResponseDto response = orderService.cancelTotalOrder(request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PatchMapping("/{totalOrderId}/orders/{orderId}/cancel")
    public ResponseEntity<CommonResponse<CancelOrderResponseDto>> cancelOrder(
        @PathVariable Long totalOrderId,
        @PathVariable Long orderId,
        HttpServletRequest httpServletRequest
    ) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        LoginUserInfoDto loginUserInfo = LoginUserInfoDto.from(passport);
        CancelOrderRequestDto request = CancelOrderRequestDto.from(totalOrderId, orderId,
            loginUserInfo);
        CancelOrderResponseDto response = orderService.cancelOrder(request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }


}
