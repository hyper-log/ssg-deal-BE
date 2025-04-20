package on.ssgdeal.order_service.presentation.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.order_service.application.service.OrderService;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.dto.UpdateCancelOrderSuccessRequestDto;
import on.ssgdeal.order_service.application.dto.UpdateTotalOrderFailRequestDto;
import on.ssgdeal.order_service.application.dto.UpdateTotalOrderSuccessRequestDto;
import on.ssgdeal.order_service.presentation.internal.dto.UpdateCancelOrderSuccessRequest;
import on.ssgdeal.order_service.presentation.internal.dto.UpdateTotalOrderFailRequest;
import on.ssgdeal.order_service.presentation.internal.dto.UpdateTotalOrderSuccessRequest;
import on.ssgdeal.order_service.presentation.internal.dto.ValidTotalOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "OrderInternalController")
@RequestMapping("/internal/v1/orders")
public class OrderInternalController {

    private final OrderService orderService;
    private final PassportUtil passportUtil;

    @PostMapping("/payments/success")
    public ResponseEntity<CommonResponse<Void>> createTotalOrderPaymentSuccess(
        @Valid @RequestBody UpdateTotalOrderSuccessRequest request,
        HttpServletRequest httpServletRequest
    ) {
        final UpdateTotalOrderSuccessRequestDto requestDto = request.toDto();
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        LoginUserInfoDto loginUserInfo = LoginUserInfoDto.from(passport);
        orderService.createTotalOrderPaymentSuccess(requestDto, loginUserInfo);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping("/valid/{totalOrderId}")
    public ResponseEntity<CommonResponse<ValidTotalOrderResponse>> validTotalOrder(
        @PathVariable Long totalOrderId) {
        ValidTotalOrderResponse response = orderService.validTotalOrder(totalOrderId);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PostMapping("/payments/fail")
    public ResponseEntity<CommonResponse<Void>> createTotalOrderPaymentFail(
        @Valid @RequestBody UpdateTotalOrderFailRequest request
    ) {
        UpdateTotalOrderFailRequestDto requestDto = request.toDto();
        orderService.createTotalOrderPaymentFail(requestDto);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @PostMapping("/payments/cancel/success")
    public ResponseEntity<CommonResponse<Void>> cancelOrderPaymentSuccess(
        @Valid @RequestBody UpdateCancelOrderSuccessRequest request
    ) {
        UpdateCancelOrderSuccessRequestDto requestDto = request.toDto();
        orderService.cancelOrderPaymentSuccess(requestDto);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
