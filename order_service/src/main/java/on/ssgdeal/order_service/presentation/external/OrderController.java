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
import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.GetTotalOrdersResponseDto;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderRequest;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "OrderController")
@RequestMapping("/api/v1/order")
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
}
